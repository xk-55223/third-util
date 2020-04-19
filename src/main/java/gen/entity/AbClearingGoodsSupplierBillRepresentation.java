package gen.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.extension.activerecord.Model;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.io.Serializable;

/**
* <p>
    * 商品供应商账单申述表
    * </p>
*
* @author keith
* @since 2020-04-17
*/
    public class AbClearingGoodsSupplierBillRepresentation extends Model<AbClearingGoodsSupplierBillRepresentation> {

    private static final long serialVersionUID = 1L;

            /**
            * 主键id
            */
            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            /**
            * 账单编号
            */
    private String billNo;

            /**
            * 申述内容
            */
    private String representation;

            /**
            * 创建时间
            */
    private Long createTime;

            /**
            * 更新时间
            */
    private Long updateTime;

            /**
            * 创建人
            */
    private String createBy;

            /**
            * 更新人
            */
    private String updateBy;

            /**
            * 位数唯一码
            */
    private String dataCode;

            /**
            * 删除标志 0有效 1删除
            */
    private Boolean delFlag;

        public Integer getId() {
        return id;
        }

            public void setId(Integer id) {
        this.id = id;
        }
        public String getBillNo() {
        return billNo;
        }

            public void setBillNo(String billNo) {
        this.billNo = billNo;
        }
        public String getRepresentation() {
        return representation;
        }

            public void setRepresentation(String representation) {
        this.representation = representation;
        }
        public Long getCreateTime() {
        return createTime;
        }

            public void setCreateTime(Long createTime) {
        this.createTime = createTime;
        }
        public Long getUpdateTime() {
        return updateTime;
        }

            public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        }
        public String getCreateBy() {
        return createBy;
        }

            public void setCreateBy(String createBy) {
        this.createBy = createBy;
        }
        public String getUpdateBy() {
        return updateBy;
        }

            public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        }
        public String getDataCode() {
        return dataCode;
        }

            public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
        }
        public Boolean getDelFlag() {
        return delFlag;
        }

            public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
    return "AbClearingGoodsSupplierBillRepresentation{" +
            "id=" + id +
            ", billNo=" + billNo +
            ", representation=" + representation +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", createBy=" + createBy +
            ", updateBy=" + updateBy +
            ", dataCode=" + dataCode +
            ", delFlag=" + delFlag +
    "}";
    }
}
