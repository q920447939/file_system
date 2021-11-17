/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年11月16日
 */
package cn.withmes.file.system;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ClassName: UploadFileResponse
 *
 * @author leegoo
 * @Description:
 * @date 2021年11月16日
 */
@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
