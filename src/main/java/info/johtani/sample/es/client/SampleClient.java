package info.johtani.sample.es.client;

public class SampleClient {


    public static void main(String[] args) {
        EsService service = new EsService();
        service.init();
        EsSearchRequest request = new EsSearchRequest("title", "コシヒカリ");
        Logger.log("starting search: " + request.printQuery());

        EsSearchResult result = service.search(request);
        result.printResults();

        Logger.log("finish searching");
    }
}
