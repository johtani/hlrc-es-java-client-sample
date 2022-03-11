package info.johtani.sample.es.client;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

public class EsService {

    RestHighLevelClient client;

    // クライアントの初期化
    public void init() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.1.240", 9200, "http")
                )
        );
    }

    public EsSearchResult search(EsSearchRequest request) {
        EsSearchResult result = new EsSearchResult();

        try {
            SearchResponse response = client.search(request.buildEsSearchResult(request), RequestOptions.DEFAULT);

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
