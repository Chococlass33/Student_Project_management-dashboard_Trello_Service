import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Card
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idList;
    private String idBoard;
    private String checkItemStates;
    private int closed;
    private Timestamp dateLastActivity;
    private String desc;
    private String descData;
    private Timestamp due;
    private int dueComplete;
    private String name;
    private Float pos;
    private String shortLink;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public Card(String id, String idList, String idBoard, String checkItemStates, int closed, Timestamp dateLastActivity, String desc, String descData, Timestamp due, int dueComplete, String name, Float pos, String shortLink, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.idList = idList;
        this.idBoard = idBoard;
        this.checkItemStates = checkItemStates;
        this.closed = closed;
        this.dateLastActivity = dateLastActivity;
        this.desc = desc;
        this.descData = descData;
        this.due = due;
        this.dueComplete = dueComplete;
        this.name = name;
        this.pos = pos;
        this.shortLink = shortLink;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected Card(){}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdList()
    {
        return idList;
    }

    public void setIdList(String idList)
    {
        this.idList = idList;
    }

    public String getIdBoard()
    {
        return idBoard;
    }

    public void setIdBoard(String idBoard)
    {
        this.idBoard = idBoard;
    }

    public String getCheckItemStates()
    {
        return checkItemStates;
    }

    public void setCheckItemStates(String checkItemStates)
    {
        this.checkItemStates = checkItemStates;
    }

    public int getClosed()
    {
        return closed;
    }

    public void setClosed(int closed)
    {
        this.closed = closed;
    }

    public Timestamp getDateLastActivity()
    {
        return dateLastActivity;
    }

    public void setDateLastActivity(Timestamp dateLastActivity)
    {
        this.dateLastActivity = dateLastActivity;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getDescData()
    {
        return descData;
    }

    public void setDescData(String descData)
    {
        this.descData = descData;
    }

    public Timestamp getDue()
    {
        return due;
    }

    public void setDue(Timestamp due)
    {
        this.due = due;
    }

    public int getDueComplete()
    {
        return dueComplete;
    }

    public void setDueComplete(int dueComplete)
    {
        this.dueComplete = dueComplete;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Float getPos()
    {
        return pos;
    }

    public void setPos(Float pos)
    {
        this.pos = pos;
    }

    public String getShortLink()
    {
        return shortLink;
    }

    public void setShortLink(String shortLink)
    {
        this.shortLink = shortLink;
    }

    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateLastModified()
    {
        return dateLastModified;
    }

    public void setDateLastModified(Timestamp dateLastModified)
    {
        this.dateLastModified = dateLastModified;
    }
}
