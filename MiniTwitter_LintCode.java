/**
 * Definition of Tweet:
 * public class Tweet {
 *     public int id;
 *     public int user_id;
 *     public String text;
 *     public static Tweet create(int user_id, String tweet_text) {
 *         // This will create a new tweet object,
 *         // and auto fill id
 *     }
 * }
 */
public class MiniTwitter {
    // imeplementation of push method of twitter
    private List<Tweet> tweetList;
    private Map<Integer, List<Integer>> followingMap;
    private Map<Integer, List<Integer>> followerMap;
    private Map<Integer, List<Integer>> newFeedsMap;
    
    public MiniTwitter() {
        // initialize your data structure here.
        tweetList = new ArrayList<>();
        followingMap = new HashMap<>();
        followerMap =  new HashMap<>();
        newFeedsMap = new HashMap<>();
    }
    
    public void addNewFeed(Integer user_id, Integer tweet_id) {
        List<Integer> newFeeds = newFeedsMap.get(user_id);
        if (newFeeds == null) {
            newFeedsMap.put(user_id, new ArrayList<>(Arrays.asList(tweet_id)));
        } else {
            newFeeds.add(tweet_id);
        }
    }
    // @param user_id an integer
    // @param tweet a string
    // return a tweet
    public Tweet postTweet(int user_id, String tweet_text) {
        // create tweet
        Tweet t = Tweet.create(user_id, tweet_text);
        tweetList.add(t);
        
        addNewFeed(user_id, t.id);
        
        if (followerMap.get(user_id) != null) {
            for (Integer followerId : followerMap.get(user_id)) {
                addNewFeed(followerId, t.id);
            }
        }
        
        return t;
    }

    // @param user_id an integer
    // return a list of 10 new feeds recently
    // and sort by timeline
    public List<Tweet> getNewsFeed(int user_id) {
        // Write your code here
        List<Tweet> newsFeeds = new ArrayList<>();
        List<Integer> newFeedsIdList = new ArrayList<>();
        if (newFeedsMap.get(user_id) != null) { 
            newFeedsIdList.addAll(newFeedsMap.get(user_id));
            for (int i = newFeedsIdList.size() - 1; i >=0 && newsFeeds.size() < 10; i--){
                newsFeeds.add(tweetList.get(newFeedsIdList.get(i) - 1));
            }
        }
        return newsFeeds;
    }
        
    // @param user_id an integer
    // return a list of 10 new posts recently
    // and sort by timeline
    public List<Tweet> getTimeline(int user_id) {
        // Write your code here
        List<Tweet> tList = new ArrayList<>();
        
        for (int i = tweetList.size() - 1; i >= 0; i--) {
	        if (tweetList.get(i).user_id == user_id) {
                tList.add(tweetList.get(i));
                if (tList.size() >= 10) {
                    break;
                }
            }
        } 
        
        return tList;
    }
    
    public List<Integer> mergeSortArray (List<Integer> aList, List<Integer> bList) {
        int m = aList.size(), n = bList.size();
        int i = 0, j = 0;
        List<Integer> cList = new ArrayList<>();
        while (i < m && j < n) {
            if (aList.get(i) < bList.get(j)) {
                cList.add(aList.get(i++));
            } else {
                cList.add(bList.get(j++));
            }
        }
        while (i < m) {
            cList.add(aList.get(i++));
        }
        while (j < n) {
            cList.add(bList.get(j++));
        }
        return cList;
    }
    
    // @param from_user_id an integer
    // @param to_user_id an integer
    // from user_id follows to_user_id
    public void follow(int from_user_id, int to_user_id) {
        if (from_user_id != to_user_id) {
            // udpate following map
            List<Integer> followingList = new ArrayList<>();
            if (followingMap.get(from_user_id) != null) {
                followingList.addAll(followingMap.get(from_user_id));
            }
            if (!followingList.contains(to_user_id)) {
                    followingList.add(to_user_id);
                    followingMap.put(from_user_id, followingList);
            }
            
            // update follower map
            List<Integer> followerList = new ArrayList<>();
            if (followerMap.get(to_user_id) != null) {
                followerList.addAll(followerMap.get(to_user_id));
            }
            if (!followerList.contains(from_user_id)) {
                    followerList.add(from_user_id);
                    followerMap.put(to_user_id, followerList);
            }
            
            // build timeline id list
            List<Integer> timeLineIdList = new ArrayList<>();
            for (int i = 0; i < tweetList.size(); i++) {
	            if (tweetList.get(i).user_id == to_user_id) {
                    timeLineIdList.add(i + 1);
                }
            }
            // build new feeds id list
            List<Integer> newFeedsIdList = new ArrayList<>();
            if (newFeedsMap.get(from_user_id) != null) {
                newFeedsIdList.addAll(newFeedsMap.get(from_user_id));
            }
            // merge timeline id list into new feeds id list
            newFeedsMap.put(from_user_id, mergeSortArray(newFeedsIdList, timeLineIdList));
        }
    }

    // @param from_user_id an integer
    // @param to_user_id an integer
    // from user_id unfollows to_user_id
    public void unfollow(int from_user_id, int to_user_id) {
        if (from_user_id != to_user_id) {
            // udpate following map
            List<Integer> followingList = followingMap.get(from_user_id); 
            if (followingList != null) {
                followingList.remove((Object)to_user_id);
            }
            
            // update follower map
            List<Integer> followerList = followerMap.get(to_user_id);
            if (followerList != null) {
                    followerList.remove((Object)from_user_id);
            }
            
            // delete new feeds for from user
            List<Integer> newFeedsList = newFeedsMap.get(from_user_id);
            if (newFeedsList != null) {
                for (int i = 0; i < newFeedsList.size(); i++) {
                    if(tweetList.get(newFeedsList.get(i) - 1).user_id == to_user_id) {
                        newFeedsList.remove(i--);
                    }
                }
            }
        }
    }
}
