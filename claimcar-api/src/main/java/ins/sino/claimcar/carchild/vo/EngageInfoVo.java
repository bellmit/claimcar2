package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 特别约定
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("ENGAGEINFO")
public class EngageInfoVo implements Serializable{

    /**  */
    private static final long serialVersionUID = 3542648689038104010L;

    @XStreamAlias("CLAUSESNAME")
    private String clausesName;//特别约定名称
    
    @XStreamAlias("CLAUSES")
    private String clauses;//特别约定内容

    
    public String getClausesName() {
        return clausesName;
    }

    
    public void setClausesName(String clausesName) {
        this.clausesName = clausesName;
    }

    
    public String getClauses() {
        return clauses;
    }

    
    public void setClauses(String clauses) {
        this.clauses = clauses;
    }
    
}
