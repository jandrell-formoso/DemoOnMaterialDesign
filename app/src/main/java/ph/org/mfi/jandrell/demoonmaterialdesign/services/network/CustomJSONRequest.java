package ph.org.mfi.jandrell.demoonmaterialdesign.services.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Jandrell on 3/27/2015.
 */
public class CustomJSONRequest extends Request<JSONObject>{

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomJSONRequest(String url, Map<String, String> params, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.params = params;
    }

    public CustomJSONRequest(int method, String url,
                             Map<String, String> params, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}
