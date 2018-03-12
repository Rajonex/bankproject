package history;

import messages.Ack;

import java.util.List;

public class History
{
    List<Ack> historyList;

    public History()
    {
        historyList = new ArrayList<Ack>();
    }

    /**
     * adding new ACK to list - history
     * @param obj
     * @return true if operation succeeded
     */
    public boolean add(Ack obj)
    {
        return historyList.add(obj);
    }

    /**
     * return list of ACKs
     *
     * TODO return non-editable version of list
     * @return list of ACKs
     */
    public List<Ack> returnList()
    {
        return historyList;
    }

}
