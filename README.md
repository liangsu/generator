## 根据数据库的表反向生成sql语句
本代码现仅支持从oracle数据库反向生成相关的sql语句

### 使用指南
找到类`Start`执行便可

###参数配置
#### 一般配置
* geterThreadNum：获取任务线程的数量
* taskSource：任务来源，是从数据库、文件还是二级制文件,值有`db`、`file`、`binFile`
* taskFile：任务文件的名称，`taskSource=file`的时候才需要配置这个参数，指定文件的位置名称如：`tables.txt`

#### 数据库连接配置
	jdbc.driverClass=oracle.jdbc.OracleDriver
	jdbc.url=jdbc:oracle:thin:@192.168.167.111:1521:Q0CGF
	jdbc.user=MSS_PURCHASE_HSS
	jdbc.password=MSS_PURCHASE_HSS


#### 数据源配置
	ds.initialSize=2
	ds.maxActive=200
	ds.minIdle=2
	ds.maxIdle=200
	ds.maxWait=1000
	ds.validationQuery=select 1 from dual

## 日志配置
> 日志配置有两个参数`debug`和`error` <br>
	log.level=debug

