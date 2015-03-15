/*
 * This file is part of Popcorn Time.
 *
 * Popcorn Time is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Popcorn Time is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Popcorn Time. If not, see <http://www.gnu.org/licenses/>.
 */

package pct.droid.base.providers.media;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.okhttp.Call;

import java.util.ArrayList;

import pct.droid.base.providers.BaseProvider;
import pct.droid.base.providers.media.models.Media;
import pct.droid.base.providers.meta.MetaProvider;

/**
 * MediaProvider.java
 * <p/>
 * Base class for all media providers. Any media providers has to extend this class and use the callback defined here.
 */
public abstract class MediaProvider extends BaseProvider implements Parcelable {
    public static final String MEDIA_CALL = "media_http_call";

    /**
     * Get a list of Media items from the provider
     *
     * @param filters  Filters the provider can use to sort or search
     * @param callback MediaProvider callback
     */
    public void getList(Filters filters, Callback callback) {
        getList(null, filters, callback);
    }

    /**
     * Get a list of Media items from the provider
     *
     * @param currentList Input the current list so it can be extended
     * @param filters     Filters the provider can use to sort or search
     * @param callback    MediaProvider callback
     * @return Call
     */
    public abstract Call getList(ArrayList<Media> currentList, Filters filters, Callback callback);

    public abstract Call getDetail(String videoId, Callback callback);

	public abstract int getLoadingMessage();

    public interface Callback {
        public void onSuccess(ArrayList<Media> items);

        public void onFailure(Exception e);
    }

    public static class Filters {
        public enum Order {ASC, DESC};
        public enum Sort {POPULARITY, YEAR, DATE, RATING, ALPHABET}

        public String keywords = null;
        public String genre = null;
        public Order order = Order.DESC;
        public Sort sort = Sort.POPULARITY;
        public Integer page = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String className = getClass().getCanonicalName();
        dest.writeString(className);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MediaProvider> CREATOR = new Parcelable.Creator<MediaProvider>() {
        @Override
        public MediaProvider createFromParcel(Parcel in) {
            String className = in.readString();
            MediaProvider provider = null;
            try {
                Class<?> clazz = Class.forName(className);
                provider = (MediaProvider) clazz.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return provider;
        }

        @Override
        public MediaProvider[] newArray(int size) {
            return null;
        }
    };

}
