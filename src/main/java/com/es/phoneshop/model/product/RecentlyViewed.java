package com.es.phoneshop.model.product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;

public class RecentlyViewed {
    public ArrayList<Product> recentlyViewed;
    public static final String RECENTLY_VIEWED_SESSION_ATTRIBUTE = RecentlyViewed.class.getName() + ".recentlyViewed";

    private static RecentlyViewed instance;

    public static synchronized RecentlyViewed getInstance() {
        if (instance == null) {
            instance = new RecentlyViewed();
        }
        return instance;
    }

    public RecentlyViewed() {
    }

    public ArrayList<Product> getQueue(HttpServletRequest request) {
        recentlyViewed = (ArrayList<Product>) request.getSession().getAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE);
        if (recentlyViewed == null) {
            recentlyViewed = new ArrayList<>();
            request.getSession().setAttribute(RECENTLY_VIEWED_SESSION_ATTRIBUTE, recentlyViewed);
        }
        return recentlyViewed;
    }

    public void add(ArrayList<Product> recentlyViewed, Product product) {
        if (!recentlyViewed.contains(product)) {
            if (recentlyViewed.size() < 3) {
                recentlyViewed.add(product);
            } else {
                for (int i = recentlyViewed.size() - 1; i > 0; i--) {
                    recentlyViewed.set(i, recentlyViewed.get(i - 1));
                }
                recentlyViewed.set(0, product);
            }
        } else Collections.swap(recentlyViewed, 0, recentlyViewed.indexOf(product));
    }
}