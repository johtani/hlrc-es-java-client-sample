package info.johtani.sample.es.client;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class AbstractEsService {

    protected RestHighLevelClient client;

    // クライアントの初期化
    public void init() {
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost("192.168.1.240", 9200, "http")
        );

        client = new RestHighLevelClient(restClientBuilder);
    }
}
