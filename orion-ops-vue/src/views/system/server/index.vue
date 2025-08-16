<template>
  <div class="app-container">
    <!-- 顶部留白 -->
    <a-row :gutter="16">
      <!-- CPU -->
      <a-col :span="12">
        <a-card title="CPU" size="small">
          <a-table
            :columns="cpuColumns"
            :data-source="cpuData"
            :pagination="false"
            :show-header="false"
            bordered
          />
        </a-card>
      </a-col>

      <!-- 内存 -->
      <a-col :span="12">
        <a-card title="内存" size="small">
          <a-table
            :columns="memColumns"
            :data-source="memData"
            :pagination="false"
            :show-header="true"
            bordered
          />
        </a-card>
      </a-col>
    </a-row>

    <!-- 服务器信息 -->
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="24">
        <a-card title="服务器信息" size="small">
          <a-row>
            <a-col :span="12">
              <strong>服务器名称：</strong>{{ server.sys && server.sys.computerName }}
            </a-col>
            <a-col :span="12">
              <strong>操作系统：</strong>{{ server.sys && server.sys.osName }}
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <strong>服务器 IP：</strong>{{ server.sys && server.sys.computerIp }}
            </a-col>
            <a-col :span="12">
              <strong>系统架构：</strong>{{ server.sys && server.sys.osArch }}
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>

    <!-- Java 虚拟机信息 -->
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="24">
        <a-card title="Java 虚拟机信息" size="small">
          <a-row>
            <a-col :span="12">
              <strong>Java 名称：</strong>{{ server.jvm && server.jvm.name }}
            </a-col>
            <a-col :span="12">
              <strong>Java 版本：</strong>{{ server.jvm && server.jvm.version }}
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <strong>启动时间：</strong>{{ server.jvm && server.jvm.startTime }}
            </a-col>
            <a-col :span="12">
              <strong>运行时长：</strong>{{ server.jvm && server.jvm.runTime }}
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <strong>安装路径：</strong>{{ server.jvm && server.jvm.home }}
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <strong>项目路径：</strong>{{ server.sys && server.sys.userDir }}
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <strong>运行参数：</strong>{{ server.jvm && server.jvm.inputArgs }}
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>

    <!-- 磁盘状态 -->
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="24">
        <a-card title="磁盘状态" size="small">
          <a-table
            :columns="diskColumns"
            :data-source="diskData"
            :pagination="false"
            bordered
          />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>

export default {
  name: 'ServerMonitor',
  data () {
    return {
      server: {}, // 统一数据
      // CPU 表
      cpuColumns: [
        { title: '属性', dataIndex: 'name', key: 'name', width: '50%' },
        { title: '值', dataIndex: 'value', key: 'value', width: '50%' }
      ],
      // 内存表
      memColumns: [
        { title: '属性', dataIndex: 'name', key: 'name' },
        { title: '物理内存', dataIndex: 'mem', key: 'mem' },
        { title: 'JVM', dataIndex: 'jvm', key: 'jvm' }
      ],
      // 磁盘表
      diskColumns: [
        { title: '盘符路径', dataIndex: 'dirName', key: 'dirName' },
        { title: '文件系统', dataIndex: 'sysTypeName', key: 'sysTypeName' },
        { title: '盘符类型', dataIndex: 'typeName', key: 'typeName' },
        { title: '总大小', dataIndex: 'total', key: 'total' },
        { title: '可用大小', dataIndex: 'free', key: 'free' },
        { title: '已用大小', dataIndex: 'used', key: 'used' },
        { title: '已用百分比', dataIndex: 'usage', key: 'usage', customRender: (text) => {
          return <span style={text > 80 ? 'color:red' : ''}>{text}%</span>
        } }
      ]
    }
  },
  computed: {
    cpuData () {
      const cpu = this.server.cpu || {}
      return [
        { key: '1', name: '核心数', value: cpu.cpuNum },
        { key: '2', name: '用户使用率', value: cpu.used + '%' },
        { key: '3', name: '系统使用率', value: cpu.sys + '%' },
        { key: '4', name: '当前空闲率', value: cpu.free + '%' }
      ]
    },
    memData () {
      const mem = this.server.mem || {}
      const jvm = this.server.jvm || {}
      return [
        { key: '1', name: '总大小', mem: mem.total + 'G', jvm: jvm.total + 'M' },
        { key: '2', name: '已用', mem: mem.used + 'G', jvm: jvm.used + 'M' },
        { key: '3', name: '剩余', mem: mem.free + 'G', jvm: jvm.free + 'M' },
        { key: '4', name: '使用率', mem: (mem.used/mem.total*100).toFixed(2) + '%', jvm: (jvm.used/jvm.total*100).toFixed(2) + '%' }
      ]
    },
    diskData () {
      return this.server.sysFiles || []
    }
  },
  mounted () {
    this.loadData()
  },
  methods: {
    loadData () {
      this.$message.loading('正在加载服务监控数据...', 0)
      this.$api.server().then(({ data }) => {
        this.server = data
        this.$message.destroy() // 关闭 loading
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 16px;
}
.text-danger {
  color: #f5222d;
}
</style>