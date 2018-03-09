package history;

import messages.Ack;

import java.util.List;

public class History {
    List<Ack> historyList;

    public boolean add(Ack obj)
    {
        return false;
    }
    public List<Ack> returnList()
    {
        return historyList;
    }

}
