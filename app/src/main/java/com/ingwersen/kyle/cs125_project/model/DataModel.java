package com.ingwersen.kyle.cs125_project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DataModel
{

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DataListItem> ITEMS = new ArrayList<DataListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DataListItem> ITEM_MAP = new HashMap<String, DataListItem>();

    private static final int COUNT = 25;

    static
    {
        // Add some sample items.
        // TODO: BUILD ACTUAL ITEMS
        for (int i = 1; i <= COUNT; i++)
        {
            addItem(createDataListItem(i));
        }
    }

    private static void addItem(DataListItem item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DataListItem createDataListItem(int position)
    {
        // TODO: BUILD ACTUAL ITEMS
        return new DataListItem(String.valueOf(position), "Item" + position, makeDetails(position));
    }

    private static String makeDetails(int position)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++)
        {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class DataListItem
    {
        public final String id;
        public final String name;
        public final String details;

        public int quantity;
        public double timeMean;
        public double timeVar;
        public LocalDateTime timeLast;
        public int utility;

        public DataItemState state;

        public DataListItem(String id, String name, String details)
        {
            this.id = id;
            this.name = name;
            this.details = details;

            this.quantity = 0;
            this.timeMean = 0;
            this.timeVar = 0;
            this.timeLast = LocalDateTime.MIN;
            this.utility = 0;

            this.state = DataItemState.SUGGESTED;
        }

        private DataListItem(String id, String name, String details, int quantity, int timeMean,
                             int timeVar, LocalDateTime timeLast, int utility, DataItemState state)
        {
            this.id = id;
            this.name = name;
            this.details = details;

            this.quantity = quantity;
            this.timeMean = timeMean;
            this.timeVar = timeVar;
            this.timeLast = timeLast;
            this.utility = utility;

            this.state = state;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public enum DataItemState
        {
            SUGGESTED, IN_CART, HIDDEN
        }
    }

}
