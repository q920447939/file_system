/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年11月16日
 */
package cn.withmes.file.system;

/**
 * ClassName: FileException
 *
 * @author leegoo
 * @Description:
 * @date 2021年11月16日
 */
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
