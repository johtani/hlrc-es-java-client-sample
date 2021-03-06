package info.johtani.sample.es.client.search;

import info.johtani.sample.es.client.AbstractEsService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

public class EsService extends AbstractEsService {

    public EsSearchResult search(EsSearchRequest request) {
        EsSearchResult result = new EsSearchResult();

        try {
            SearchResponse response = client.search(request.buildEsSearchResult(), RequestOptions.DEFAULT);

            if(validResStatus(response)) {
                result.fromEsResponse(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // ちゃんとチェックしないとダメ
    private boolean validResStatus(SearchResponse response) {
        RestStatus status = response.status();
        //TODO check partial or not
        return status.getStatus() == RestStatus.OK.getStatus();
    }
}
