/* See full code here: https://github.com/leanix/leanix-sdk-java/
blob/develop/samples/console/src/main/java/ServicesTest.java */

import net.leanix.api.common.*
import net.leanix.api.ServicesApi;
import net.leanix.api.models.Service;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
// import others ...

public class JsonToLeanIXImporter {
    public static void main(String[] args) {
        try {
            // initialize
            ApiClient apiClient = new ApiClientBuilder()
                .withBasePath("https://app.leanix.net/demo/api/v1")
                .withTokenProviderHost("app.leanix.net")
                .withApiToken("NOnrUpMXEh87xbDCYkLfrBmfbzLOFznjqVqEbNMp")
                .build();
            // access the applications API
            ServicesApi servicesApi = new ServicesApi(apiClient);
            // provide JSON
            InputStream is = new FileInputStream("ListOfMicroservicesToImport.json");
            JSONObject json = new JSONObject(IOUtils.toString(is));
            // import data to LeanIX
            service = createOrUpdateService(json);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

	private Service createOrUpdateService(JSONObject json) throws ApiException {
		String name = json.getString("name");
		List<Service> services = servicesApi.getServices(true, name);
    		// find if such Application already exists in LeanIX
		Service service = null;
		for (Service existingService : services) {
			if (name.equals(existingService.getName())) {
				service = existingService;
				break;
			}
		}
		if (service == null) {
      			// create a new Application
			service = new Service();
			service.setName(name);
			service.setDescription(json.toString());
			service = servicesApi.createService(service);
		} else {
      			// update an existing Application
			service.setName(name);
			service.setDescription(json.toString());
			service = servicesApi.updateService(service.getID(), service);
		}

		return service;
	}
}
