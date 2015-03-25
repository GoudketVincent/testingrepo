package edu.ap.jaxrs;

import java.io.*;
import java.util.*;



import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.xml.bind.*;

@RequestScoped
@Path("/products")
public class ProductResource {
	
	@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		String htmlString = "<html><body>";
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("C:/Users/Vincent/Documents/school/Webtech 3/Product.json");
			ProductsJSON ProductsJSON = (ProductsJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = ProductsJSON.getProducts();
			
			for(Product product : listOfProducts) {
				htmlString += "<b>ShortName : " + product.getShortname() + "</b><br>";
				htmlString += "Id : " + product.getId() + "<br>";
				htmlString += "Brand : " + product.getBrand() + "<br>";
				htmlString += "Description : " + product.getDescription() + "<br>";
				htmlString += "Price : " + product.getPrice() + "<br>";
				htmlString += "<br><br>";
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return htmlString;
	}
	
	@GET
	@Produces({"application/json"})
	public String getProductsJSON() {
		String jsonString = "{\"products\" : [";
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("C:/Users/Vincent/Documents/school/Webtech 3/Product.json");
			ProductsJSON productsJSON = (ProductsJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productsJSON.getProducts();
			
			for(Product product : listOfProducts) {
				jsonString += "{\"shortname\" : \"" + product.getShortname() + "\",";
				jsonString += "\"id\" : " + product.getId() + ",";
				jsonString += "\"brand\" : \"" + product.getBrand() + "\",";
				jsonString += "\"description\" : \"" + product.getDescription() + "\",";
				jsonString += "\"price\" : " + product.getPrice() + "},";
			}
			jsonString = jsonString.substring(0, jsonString.length()-1);
			jsonString += "]}";
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	

	@GET
	@Path("/{shortname}")
	@Produces({"application/json"})
	public String getProductJSON(@PathParam("shortname") String shortname) {
		String jsonString = "";
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("C:/Users/Vincent/Documents/school/Webtech 3/Product.json");
			ProductsJSON productsJSON = (ProductsJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productsJSON.getProducts();
			
			// look for the product, using the shortname
			for(Product product : listOfProducts) {
				if(shortname.equalsIgnoreCase(product.getShortname())) {
					jsonString += "{\"shortname\" : \"" + product.getShortname() + "\",";
					jsonString += "\"id\" : " + product.getId() + ",";
					jsonString += "\"brand\" : \"" + product.getBrand() + "\",";
					jsonString += "\"description\" : \"" + product.getDescription() + "\",";
					jsonString += "\"price\" : " + product.getPrice() + "}";
				}
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	
	@GET
	@Path("/{shortname}")
	@Produces({"text/json"})
	public String getproductJSON(@PathParam("shortname") String shortname) {
		String jsonString = "";
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("C:/Users/Vincent/Documents/school/Webtech 3/Product.json");
			ProductsJSON productsJSON = (ProductsJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productsJSON.getProducts();
			
			// look for the product, using the shortname
			for(Product product : listOfProducts) {
				if(shortname.equalsIgnoreCase(product.getShortname())) {
					JAXBContext jaxbContext2 = JAXBContext.newInstance(Product.class);
					Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
					StringWriter sw = new StringWriter();
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					jaxbMarshaller.marshal(product, sw);
					jsonString = sw.toString();
				}
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	
	@POST
	@Consumes({"text/JSON"})
	public void processFromJSON(String productJSON) {
		
		/* newproductJSON should look like this :
		 *  
		 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 <product>
        	<brand>BRAND</brand>
        	<description>DESCRIPTION</description>
        	<id>123456</id>
        	<price>20.0</price>
        	<shortname>SHORTNAME</shortname>
        	<sku>SKU</sku>
		 </product>
		 */
		
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsJSON.class);
			Unmarshaller jaxbUnmarshaller1 = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("C:/Users/Vincent/Documents/school/Webtech 3/Product.json");
			ProductsJSON ProductsJSON = (ProductsJSON)jaxbUnmarshaller1.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = ProductsJSON.getProducts();
			
			// unmarshal new product
			JAXBContext jaxbContext2 = JAXBContext.newInstance(Product.class);
			Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
			StringReader reader = new StringReader(productJSON);
			Product newProduct = (Product)jaxbUnmarshaller2.unmarshal(reader);
			
			// add product to existing product list 
			// and update list of products in  ProductsJSON
			listOfProducts.add(newProduct);
			ProductsJSON.setProducts(listOfProducts);
			
			// marshal the updated ProductsJSON object
			Marshaller jaxbMarshaller = jaxbContext1.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(ProductsJSON, JSONfile);
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
	}
}