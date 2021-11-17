/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年11月16日
 */
package cn.withmes.file.system;

/**
 * ClassName: FileProperties
 *
 * @Description:
 * @author leegoo
 * @date 2021年11月16日
 */

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
