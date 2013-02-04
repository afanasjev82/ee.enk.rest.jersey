package ee.enk.rest.jersey.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unchecked"})
public enum DataProvider {
	instance;
	private static final String TIP_FILE="F:\\workspace\\NOMX_Comm_Close2.tip";
	private static Map<Long, Tradable> tradableContainer = new HashMap<Long, Tradable>();
	private static final Map<String, List<PairEntry<String,String>>> fieldsMap;
	
	//static (class) initializer
    static
    {
    	fieldsMap = new HashMap<String, List<PairEntry<String,String>>>();

    	fieldsMap.put("BDDe", Arrays.asList(new PairEntry<String,String>("EXb","setStartDeliveryDate"), 
    										new PairEntry<String,String>("EXe","setStopDeliveryDate")));
    	fieldsMap.put("BDt", Arrays.asList(new PairEntry<String,String>("LDa","setStartTradingDate"), 
    										new PairEntry<String,String>("TTd","setStopTradingDate")));
  	
    	fieldsMap.put("OBs", Arrays.asList(new PairEntry<String,String>("BPr","setBestBid"),
    										new PairEntry<String,String>("APl","setBestAsk"),
    										new PairEntry<String,String>("o","setContracts"),
    										new PairEntry<String,String>("Ph","setHighestTradedPrice"),
    										new PairEntry<String,String>("LOp","setLowestTradedPrice"),
    										new PairEntry<String,String>("SEp","setClosingPrice")));    
    	initData();
    }

    private DataProvider() {}
   
    //return whole model
    public Map<Long, Tradable> getModel(){
           return tradableContainer;
    }
    //return all Tradable objecy Id's
    public Set<Long> getAllTradableId(){
    	return tradableContainer.keySet();
    	
    }
    
    //return Tradable object
    public Tradable getTradable(String id) throws NotFoundTradableException{
    	if(existTradable(id))
    		return tradableContainer.get(Long.parseLong(id));
    	else throw new NotFoundTradableException("Tradable item (Resource ID #"+id+") not found!");
    }
    
    public boolean existTradable(String id){
    	//System.out.println("ID: "+id+" "+tradableContainer.containsKey(Long.parseLong(id)));
    	return tradableContainer.containsKey(Long.parseLong(id));
    }

    private static void initData(){
 	
		BufferedReader br = null;
		
		try {
				
 			String line;

			br = new BufferedReader(new FileReader(TIP_FILE));
			Tradable t;
			while ((line = br.readLine()) != null) {
				
				String messageType = line.substring(0,line.indexOf(';'));
				
				if(fieldsMap.containsKey(messageType)){
					//get all TIP fields of current TIP message type
					List<PairEntry<String,String>> fields = fieldsMap.get(messageType);
					
					//find tradable id
					Long id = Long.parseLong(getFieldValue(line, "i"));
					
					//get tradable object
					if(tradableContainer.containsKey(id)){
						t = tradableContainer.get(id);
					}else{									
						t = new Tradable(id);
					}
		
					//invoke corresponding method
					Class<?> c = t.getClass();
					Class<?>[] paramTypes = new Class[]{String.class};
					
					//iterate over all TIP message fields and update Tradable bean
					Iterator<PairEntry<String, String>> fi = fields.iterator();
					while(fi.hasNext()){
						PairEntry<String, String> fieldMethod = fi.next();
						//get TIP field value
						String value = getFieldValue(line, fieldMethod.getKey());
											
						if(existField(line,fieldMethod.getKey())){	
							Object[] methodArgs = new Object[] {value}; 
								try {
									//get corresponding Tradable bean setter method
									Method method = c.getMethod(fieldMethod.getValue(),paramTypes);
									//execute setter
									method.invoke(t, methodArgs);
									
								} catch (SecurityException e) {
									e.printStackTrace();
									System.exit(0);
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
									System.exit(0);
								} catch (IllegalArgumentException e) {								
									e.printStackTrace();
									System.exit(0);
								} catch (IllegalAccessException e) {								
									e.printStackTrace();
									System.exit(0);
								} catch (InvocationTargetException e) {								
									e.printStackTrace();
									System.exit(0);
								}
						}
						
					}
					
					//if(id==97765) System.out.println(t);
					
					//put Tradable object to pool
					tradableContainer.put(t.getId(),t);
				}
		
			}
			//System.out.println("Element count: "+tradableContainer.size());	
			
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
	
    }
    
	private static String getFieldValue(String line,String prefix){
			
			Pattern MY_PATTERN = Pattern.compile(";"+prefix+"([0-9.]*);");
			Matcher matcher = MY_PATTERN.matcher(line);
			
			String fieldValue = null;
			
			while (matcher.find()) {			      
				fieldValue = matcher.group(1);
			}
			return fieldValue;
			
			/*if(line.matches(".*;"+prefix+"[0-9]*;.*")){
				System.out.println("Field '"+prefix+"' found!");
			}else{
				System.out.println("Field '"+prefix+"' NOT found!");
			}*/

	}
	private static boolean existField(String line,String prefix){
		return line.matches("(.*);"+prefix+"([0-9.]*);(.*)");
		
	}
	
    /*public static void main(String[] args) { 
    	DataProvider.instance.getModel();
    }*/

}