package online.iiitd.edu.in.iiitd_online.usercommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class UserCommunityContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<UserCommunityItem> ITEMS = new ArrayList<UserCommunityItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, UserCommunityItem> ITEM_MAP = new HashMap<String, UserCommunityItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(UserCommunityItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static UserCommunityItem createDummyItem(int position) {
        return new UserCommunityItem(String.valueOf(position), "Item " + position);
    }


    public static class UserCommunityItem {
        public final String id;
        public final String name;

        public UserCommunityItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
