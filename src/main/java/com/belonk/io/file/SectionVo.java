package main.java.com.belonk.io.file;

/**
 * 特价机票使用航班的区间信息
 * Created by xiahuaze on 2017/9/20.
 */
public class SectionVo {
    private String id;
    private String ticketId;//机票id
    private Integer start_section;//开始区间
    private Integer end_section;//结束区间

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStart_section() {
        return start_section;
    }

    public void setStart_section(Integer start_section) {
        this.start_section = start_section;
    }

    public Integer getEnd_section() {
        return end_section;
    }

    public void setEnd_section(Integer end_section) {
        this.end_section = end_section;
    }
}
