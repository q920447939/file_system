## 輕量級文件管理系統（开箱即用）

### 使用场景：

对于需要充当临时网络文件服务器。省去了其余不需要的功能。仅保留基础功能！

### 依赖说明：

|       | 源码版本 | jar包版本 |
| ----- | -------- | --------- |
| JDK   | 1.8以上  | 1.8以上   |
| maven | 需要     | 不需要    |

### 

### 效果图：

![image-20211117133653253](./image-20211117133653253.png)

### 

### 功能说明：

|   上传   |  √   |
| :------: | :--: |
|   下载   |  √   |
|   删除   |  √   |
|   查看   |  √   |
| 进度查看 |  √   |



### 使用說明：


#### jar包版本

```java
1.下载对应的`jar`包

2.java -jar file_system-0.0.1-SNAPSHOT.jar

3.浏览器访问 http://127.0.0.1:8081即可进行操作
```





#### 源代码版本

```
1. git clone https://github.com/q920447939/file_system
2. mvn clean package
3. java -jar file_system-0.0.1-SNAPSHOT.jar
4. 浏览器访问 http://127.0.0.1:8081即可进行操作
```
