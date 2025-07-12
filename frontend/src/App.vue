<template>
  <div id="app">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1><el-icon><Search /></el-icon> Meta Tag Scanner</h1>
          <p>Scan websites and analyze meta tags with advanced filtering</p>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <el-card class="scanner-card">
          <template #header>
            <div class="card-header">
              <span>Website Scanner</span>
            </div>
          </template>
          
          <!-- URL Input -->
          <el-form :model="form" label-width="120px" class="scanner-form">
            <el-form-item label="Website URL">
              <el-input 
                v-model="form.url" 
                placeholder="https://example.com"
                :prefix-icon="Link"
                clearable
              />
            </el-form-item>
            
            <!-- Filters Section -->
            <el-form-item label="Filters">
              <el-button @click="addFilter" type="primary" :icon="Plus" size="small">
                Add Filter
              </el-button>
              
              <div v-for="(filter, index) in form.filters" :key="index" class="filter-item">
                <el-card class="filter-card">
                  <template #header>
                    <div class="filter-header">
                      <span>Filter {{ index + 1 }}</span>
                      <el-button @click="removeFilter(index)" type="danger" :icon="Delete" size="small" text>
                        Remove
                      </el-button>
                    </div>
                  </template>
                  
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="Name">
                        <el-input v-model="filter.name" placeholder="meta name" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="Content">
                        <el-input v-model="filter.content" placeholder="meta content" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-card>
              </div>
            </el-form-item>
            
            <!-- Action Buttons -->
            <el-form-item>
              <el-button @click="scanWebsite" type="primary" :icon="Search" :loading="loading">
                Scan Website
              </el-button>
              <el-button @click="exportResults" type="success" :icon="Download" :disabled="!scanResults">
                Export Results
              </el-button>
              <el-button @click="clearResults" type="info" :icon="Refresh" :disabled="!scanResults">
                Clear Results
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- Results Section -->
        <div v-if="scanResults" class="results-section">
          <el-card class="results-card">
            <template #header>
              <div class="card-header">
                <span>Scan Results</span>
                <div class="results-info">
                  <el-tag type="info">URL: {{ scanResults.url }}</el-tag>
                  <el-tag type="success">Total Meta Tags: {{ scanResults.allMetaTags.length }}</el-tag>
                  <el-tag type="warning">Matching: {{ scanResults.matchingMetaTags.length }}</el-tag>
                  <el-tag type="primary">Scan Time: {{ scanResults.scanTime }}ms</el-tag>
                </div>
              </div>
            </template>
            
            <!-- Error Display -->
            <el-alert 
              v-if="scanResults.error" 
              :title="scanResults.error" 
              type="error" 
              show-icon 
              :closable="false"
            />
            
            <!-- All Meta Tags -->
            <el-collapse v-model="activeNames">
              <el-collapse-item title="All Meta Tags" name="all">
                <el-table :data="scanResults.allMetaTags" stripe style="width: 100%">
                  <el-table-column prop="name" label="Name" width="200" />
                  <el-table-column prop="property" label="Property" width="200" />
                  <el-table-column prop="content" label="Content" show-overflow-tooltip />
                  <el-table-column prop="httpEquiv" label="Http-Equiv" width="150" />
                  <el-table-column prop="charset" label="Charset" width="100" />
                </el-table>
              </el-collapse-item>
              
              <el-collapse-item title="Matching Meta Tags" name="matching">
                <el-table :data="scanResults.matchingMetaTags" stripe style="width: 100%">
                  <el-table-column prop="name" label="Name" width="200" />
                  <el-table-column prop="property" label="Property" width="200" />
                  <el-table-column prop="content" label="Content" show-overflow-tooltip />
                  <el-table-column prop="httpEquiv" label="Http-Equiv" width="150" />
                  <el-table-column prop="charset" label="Charset" width="100" />
                </el-table>
              </el-collapse-item>
            </el-collapse>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default {
  name: 'App',
  setup() {
    // Default Dublin Core meta tag names
    const defaultMetaNames = [
      'DC.Title',
      'DC.Creator', 
      'DC.Date',
      'DC.Publisher',
      'DC.Description',
      'DC.Identifier',
      'DC.Language',
      'DC.Source',
      'DC.Contributor',
      'DC.Subject',
      'DC.Coverage',
      'DC.Type',
      'DC.Format',
      'DC.Relation',
      'DC.Rights'
    ]

    const form = reactive({
      url: '',
      filters: defaultMetaNames.map(name => ({
        name: name,
        content: ''
      })),
      outputFormat: 'json'
    })
    
    const scanResults = ref(null)
    const loading = ref(false)
    const activeNames = ref(['matching'])
    
    const addFilter = () => {
      form.filters.push({
        name: '',
        content: ''
      })
    }
    
    const removeFilter = (index) => {
      form.filters.splice(index, 1)
    }
    
    const scanWebsite = async () => {
      if (!form.url) {
        ElMessage.error('Please enter a website URL')
        return
      }
      
      loading.value = true
      
      try {
        const response = await axios.post('/api/scan', {
          url: form.url,
          filters: form.filters,
          outputFormat: form.outputFormat
        })
        
        scanResults.value = response.data
        ElMessage.success('Website scanned successfully!')
      } catch (error) {
        console.error('Scan error:', error)
        ElMessage.error(error.response?.data?.error || 'Failed to scan website')
      } finally {
        loading.value = false
      }
    }
    
    const exportResults = async () => {
      if (!scanResults.value) {
        ElMessage.error('No results to export')
        return
      }
      
      try {
        const response = await axios.post('/api/export', {
          url: form.url,
          filters: form.filters,
          outputFormat: form.outputFormat
        }, {
          responseType: 'blob'
        })
        
        const url = window.URL.createObjectURL(new Blob([response.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `meta-tags-${Date.now()}.${form.outputFormat}`)
        document.body.appendChild(link)
        link.click()
        link.remove()
        
        ElMessage.success('Results exported successfully!')
      } catch (error) {
        console.error('Export error:', error)
        ElMessage.error('Failed to export results')
      }
    }
    
    const clearResults = () => {
      scanResults.value = null
      ElMessage.info('Results cleared')
    }
    
    return {
      form,
      scanResults,
      loading,
      activeNames,
      addFilter,
      removeFilter,
      scanWebsite,
      exportResults,
      clearResults
    }
  }
}
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding: 20px 0;
}

.header-content {
  text-align: center;
  color: #2c3e50;
}

.header-content h1 {
  margin: 0 0 10px 0;
  font-size: 2.5rem;
  font-weight: 300;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.header-content p {
  margin: 0;
  font-size: 1.1rem;
  color: #666;
}

.main-content {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.scanner-card {
  margin-bottom: 30px;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 1.2rem;
}

.scanner-form {
  max-width: 800px;
}

.filter-item {
  margin-top: 20px;
}

.filter-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.results-section {
  margin-top: 30px;
}

.results-card {
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.results-info {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-collapse {
  border: none;
}

.el-collapse-item__header {
  font-weight: 600;
  font-size: 1.1rem;
}

@media (max-width: 768px) {
  .header-content h1 {
    font-size: 2rem;
  }
  
  .main-content {
    padding: 20px 10px;
  }
  
  .results-info {
    flex-direction: column;
  }
}
</style> 