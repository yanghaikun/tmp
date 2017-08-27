package net.rlair.flight.common;

/**
 * Created by Kevin on 2017/8/25.
 */
public enum RecordStatus {
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 取消
     */
    CANCEL,

    /**
     * 恢复
     */
    RECOVER,

    /**
     * 已应用
     */
    FINISH,

    //
    NONE;
}
