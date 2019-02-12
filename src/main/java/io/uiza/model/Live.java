package io.uiza.model;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.uiza.exception.BadRequestException;
import io.uiza.exception.UizaException;
import io.uiza.net.ApiResource;
import io.uiza.net.util.ErrorMessage;

public class Live extends ApiResource {
  private static final String CLASS_DEFAULT_PATH = "live/entity";
  private static final String FEED_PATH = "feed";
  private static final String LIVE_VIEW_PATH = "tracking/current-view";
  private static final String RECORD_PATH = "dvr";
  private static final String VOD_PATH = "dvr/convert-to-vod";

  public enum Mode {
    @SerializedName("pull")
    PULL("pull"),

    @SerializedName("push")
    PUSH("push");

    private String mode;

    private Mode(String mode) {
      this.mode = mode;
    }

    public String getMode() {
      return mode;
    }
  }

  public enum Encode {
    @SerializedName("0")
    NO_ENCODE(0),

    @SerializedName("1")
    ENCODE(1);

    private int encode;

    private Encode(int encode) {
      this.encode = encode;
    }

    public int getEncode() {
      return encode;
    }
  }

  public enum Dvr {
    @SerializedName("0")
    NO_RECORD(0),

    @SerializedName("1")
    ACTIVE_RECORD(1);

    private int dvr;

    private Dvr(int dvr) {
      this.dvr = dvr;
    }

    public int getDvr() {
      return dvr;
    }
  }

  public enum ResourceMode {
    @SerializedName("single")
    SINGLE("single"),

    @SerializedName("redundant")
    REDUNDANT("redundant");

    private String resourceMode;

    private ResourceMode(String resourceMode) {
      this.resourceMode = resourceMode;
    }

    public String getResourceMode() {
      return resourceMode;
    }
  }

  /**
   * Create a live streaming and manage the live streaming input (output).
   * A live stream can be set up and start later or start right after set up.
   * Live Channel Minutes counts when the event starts.
   *
   * @param liveParams a Map object storing key-value pairs of request parameter
   *
   */
  public static JsonObject create(Map<String, Object> liveParams) throws UizaException {
    JsonElement response =
        request(RequestMethod.POST, buildRequestURL(CLASS_DEFAULT_PATH), liveParams);
    String id = getId((JsonObject) checkResponseType(response));

    return retrieve(id);
  }

  /**
   * Retrieves the details of an existing event.
   *
   * @param id An id of live event to retrieve
   *
   */
  public static JsonObject retrieve(String id) throws UizaException {
    if (id == null || id.isEmpty()) {
      throw new BadRequestException(ErrorMessage.BAD_REQUEST_ERROR, "", 400);
    }

    Map<String, Object> liveParams = new HashMap<>();
    liveParams.put("id", id);
    JsonElement response =
        request(RequestMethod.GET, buildRequestURL(CLASS_DEFAULT_PATH), liveParams);

    return checkResponseType(response);
  }

  /**
   * Start a live event that has been create success.
   * The Live channel minute start count whenever the event start success.
   *
   * @param id An id of live event to start feeding
   *
   */
  public static JsonObject startFeed(String id) throws UizaException {
    String path_extension = String.format("%s/%s", CLASS_DEFAULT_PATH, FEED_PATH);
    Map<String, Object> liveParams = new HashMap<>();
    liveParams.put("id", id);
    JsonElement response = request(RequestMethod.POST, buildRequestURL(path_extension), liveParams);

    return checkResponseType(response);
  }

  /**
   * Get a live view status.
   * This view only show when event has been started and being processing.
   *
   * @param id An id of live event to get view
   *
   */
  public static JsonObject getView(String id) throws UizaException {
    String path_extension = String.format("%s/%s", CLASS_DEFAULT_PATH, LIVE_VIEW_PATH);
    Map<String, Object> liveParams = new HashMap<>();
    liveParams.put("id", id);
    JsonElement response = request(RequestMethod.GET, buildRequestURL(path_extension), liveParams);

    return checkResponseType(response);
  }

  /**
   * Retrieves list of recorded file after streamed
   * (only available when your live event has turned on Record feature)
   *
   */
  public static JsonArray listRecorded() throws UizaException {
    String path_extension = String.format("%s/%s", CLASS_DEFAULT_PATH, RECORD_PATH);
    JsonElement response = request(RequestMethod.GET, buildRequestURL(path_extension), null);

    return checkResponseType(response);
  }

  /**
   * Convert recorded file into VOD entity.
   * After converted, your file can be stream via Uiza's CDN.
   *
   * @param id An id of live event to convert into VOD
   */
  public static JsonObject convertToVod(String id) throws UizaException {
    String path_extension = String.format("%s/%s", CLASS_DEFAULT_PATH, VOD_PATH);
    Map<String, Object> liveParams = new HashMap<>();
    liveParams.put("id", id);
    JsonElement response = request(RequestMethod.POST, buildRequestURL(path_extension), liveParams);

    return checkResponseType(response);
  }
}
