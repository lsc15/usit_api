package com.usit.app.util.naverpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.domain.DeliveryFee;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;
import com.usit.domain.UsitOrderItem;
import com.usit.domain.UsitOrderTransaction;


public class NpayOrder {
	private static Logger LOGGER = LoggerFactory.getLogger(NpayOrder.class);
	private final String ENCODING = "UTF-8";
	
	private final String ORDER = "order";
	private final String PRODUCT = "product";
	private final String PRODUCT_ID = "id";
	private final String PRODUCT_NAME = "name";
	private final String PRODUCT_BASEPRICE = "basePrice";
	private final String PRODUCT_TAXTYPE = "taxType";
	private final String PRODUCT_TAXTYPE_TAX = "TAX";
//	private final String PRODUCT_TAXTYPE_TAXFREE = "TAX_FREE";
//	private final String PRODUCT_TAXTYPE_ZEROTAX = "ZERO_TAX";
	
	private final String PRODUCT_INFOURL = "infoUrl";
	private final String PRODUCT_IMGURL = "imageUrl";
	private final String PRODUCT_SINGLE = "single";
	private final String PRODUCT_SINGLE_QUANTITY = "quantity";
	private final String PRODUCT_URL = "https://usit.co.kr/product/";
	
	private final String BACKURL = "backUrl";
	
	
	
	
	private final String OPTION = "option";
	private final String OPTION_QUANTITY = "quantity";
	private final String OPTION_PRICE = "price";
	private final String OPTION_MANAGECODE = "manageCode";
	private final String OPTION_SELECTEDITEM = "selectedItem";
	private final String OPTION_SELECTEDITEM_TYPE = "type";
	private final String OPTION_SELECTEDITEM_TYPE_SELECT = "SELECT";
	private final String OPTION_SELECTEDITEM_NAME = "name";
	
	private final String OPTION_SELECTEDITEM_VALUE = "value";
	private final String OPTION_SELECTEDITEM_VALUE_ID = "id";
	private final String OPTION_SELECTEDITEM_VALUE_TEXT = "text";
	private final String MERCHANTID = "merchantId";
    private final String CERTIKEY = "certiKey";
	
	
	private final String SHIPPINGPOLICY = "shippingPolicy";
	private final String SHIPPINGPOLICY_GRUOPID = "groupId";
	private final String SHIPPINGPOLICY_METHOD = "method";
	private final String SHIPPINGPOLICY_METHOD_DELIVERY = "DELIVERY";
	private final String SHIPPINGPOLICY_FEETYPE = "feeType";
	private final String SHIPPINGPOLICY_FEETYPE_FREE = "FREE";
	private final String SHIPPINGPOLICY_FEETYPE_CHARGE = "CHARGE";
	private final String SHIPPINGPOLICY_FEETYPE_CONDITIONAL_FREE = "CONDITIONAL_FREE";
	private final String SHIPPINGPOLICY_FEEPAYTYPE = "feePayType";
	private final String SHIPPINGPOLICY_FEEPAYTYPE_PREPAYED = "PREPAYED";
	private final String SHIPPINGPOLICY_FEEPRICE = "feePrice";
	private final String SHIPPINGPOLICY_CONDITIONALFREE = "conditionalFree";
	private final String SHIPPINGPOLICY_CONDITIONALFREE_BASEPRICE = "basePrice";
	
	
	
	/**
	 * 상품정보 연동
	 */
	private final String PRODUCTS = "products";
	private final String PRODUCT_STOCK_QUANTITY = "stockQuantity";
	private final String PRODUCT_RETURN_SHIPPING_FEE = "returnShippingFee";
	private final String PRODUCT_EXCHANGE_SHIPPING_FEE = "exchangeShippingFee";
	private final String PRODUCT_RETURN_INFO = "returnInfo";
	
	private final String RETURN_INFO_ZIPCODE = "zipcode";
	private final String RETURN_INFO_ADDRESS1 = "address1";
	private final String RETURN_INFO_ADDRESS2 = "address2";
	private final String RETURN_INFO_SHELLERNAME = "sellername";
	private final String RETUNR_INFO_CONTACT1 = "contact1";
	
	private final String OPTION_OPTIONITEM = "optionItem";
	
	
	
	private final String OPTION_COMBINATION = "combination";
	private final String OPTION_COMBINATION_MANAGECODE = "manageCode";
	private final String OPTION_COMBINATION_OPTIONS = "options";
	
	
	
	private final String RESERVE1 = "RESERVE1";
	private final String RESERVE2 = "RESERVE2";
	private final String RESERVE3 = "RESERVE3";
	private final String RESERVE4 = "RESERVE4";
	private final String RESERVE5 = "RESERVE5";
	private final String SA_CLICK_ID = "SA_CLICK_ID"; // CTS
	public URL _url;
	private SSLSocketFactory _sslSockFactory;

	public NpayOrder()

	{
		_url = null;
	}

	public NpayOrder(URL url) {
		_url = url;
		_initHttps();
	}

	public NpayOrder(String url) throws MalformedURLException {
		this(new URL(url));
	}

	public void setUrl(String url) throws MalformedURLException {
		_url = new URL(url);
	}

	public static class ItemStack {
		private String _itemId;
		private String _itemName;
		private int _itemTPrice;
		private int _itemUPrice;
		private String _selectedOption;
		private int _count;

		/**
		 * @param itemId
		 *            Mall Item Code
		 * @param itemName
		 *            상품
		 * @param itemPrice
		 *            상품 개별 가격
		 * @param selectedOption
		 *            선택된 옵션. - 여러 옵션이 선택되었을 경우 '/'로 구분하는 것을 권장
		 * @param count
		 *            해당 상품 구매 갯수.
		 */
		public ItemStack(String itemId, String itemName, int itemTPrice, int itemUPrice, String selectedOption,
				int count) {
			_itemId = itemId;
			_itemName = itemName;
			_itemTPrice = itemTPrice;
			_itemUPrice = itemUPrice;
			_selectedOption = selectedOption;
			_count = count;
		}

		public String getItemId() {
			return _itemId;
		}

		public String getItemName() {
			return _itemName;
		}

		public int getItemTotalPrice() {
			return _itemTPrice;
		}

		public int getItemUnitPrice() {
			return _itemUPrice;
		}

		public String getSelectedOption() {
			if (_selectedOption == null)
				return "";
			return _selectedOption;

		}

		public int getCount() {
			return _count;
		}
	}

	private void _urlEncode(StringBuffer sb, String key, String value) {
		try {
			sb.append(URLEncoder.encode(key, ENCODING));
			sb.append('=');
			sb.append(URLEncoder.encode(value, ENCODING));
		} catch (UnsupportedEncodingException e) {
			// 일어나지 않음
			throw new Error(e);
		}
	}

//	private String _makeQueryString(String merchantId, String certificationKey, ItemStack[] items, int shippingPrice,
//			String shippingType, String backURL) {
////			String shippingType, String backURL, String saClickId, String cpaInflowCode, String naverInflowCode) {
//		// 주문 금액=각 상품 금액+배송비(단 선불일 경우)
//		int totalPrice = shippingPrice > 0 ? shippingPrice : 0;
//		StringBuffer sb = new StringBuffer();
//		_urlEncode(sb, NCKEY_MERCHANTID, merchantId);
//		sb.append('&');
//		_urlEncode(sb, NCKEY_CERTIKEY, certificationKey);
//		sb.append('&');
//		for (ItemStack is : items) {
//			totalPrice += is.getItemTotalPrice();
//			_urlEncode(sb, NCKEY_ITEMID, is.getItemId());
//			sb.append('&');
//			_urlEncode(sb, NCKEY_ITEMNAME, is.getItemName());
//			sb.append('&');
//			_urlEncode(sb, NCKEY_TPRICE, String.valueOf(is.getItemTotalPrice()));
//			sb.append('&');
//			_urlEncode(sb, NCKEY_UPRICE, String.valueOf(is.getItemUnitPrice()));
//			sb.append('&');
//			_urlEncode(sb, NCKEY_COUNT, String.valueOf(is.getCount()));
//			sb.append('&');
//			_urlEncode(sb, NCKEY_OPTION, is.getSelectedOption());
//			sb.append('&');
//		}
//		_urlEncode(sb, NCKEY_SHIPPINGTYPE, shippingType);
//		sb.append('&');
//		_urlEncode(sb, NCKEY_SHIPPINGPRICE, String.valueOf(shippingPrice));
//		sb.append('&');
//		_urlEncode(sb, NCKEY_TOTALPRICE, String.valueOf(totalPrice));
//		sb.append('&');
//		_urlEncode(sb, NCKEY_BACKURL, backURL);
//		sb.append('&');
//		_urlEncode(sb, NCKEY_RESERVE1, "");
//		sb.append('&');
//		_urlEncode(sb, NCKEY_RESERVE2, "");
//		sb.append('&');
//		_urlEncode(sb, NCKEY_RESERVE3, "");
//		sb.append('&');
//		_urlEncode(sb, NCKEY_RESERVE4, "");
//		sb.append('&');
//		_urlEncode(sb, NCKEY_RESERVE5, "");
//		sb.append('&'); // CTS
//		//_urlEncode(sb, NCKEY_SA_CLICK_ID, saClickId); // CTS
//		// CPA 스크립트 가이드 설치업체는 해당 값 전달 sb.append('&');
//		// _urlEncode(sb, NCKEY_CPA_INFLOW_CODE, cpaInflowCode); sb.append('&');
//		//_urlEncode(sb, NCKEY_NAVER_INFLOW_CODE, naverInflowCode);
//		System.out.println(sb.toString());
//		return sb.toString();
//	}

	/* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */ private void _initHttps() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} };
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			_sslSockFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			re.setStackTrace(e.getStackTrace());
			throw re;
		}
	}

	/**
	 * @param items
	 *            주문 상품 목록.
	 * @param shippingPrice
	 *            배송비.
	 * @param shippingType
	 *            배송비결제 구분. "FREE": 무료. "PAYED": 선불. "ONDELIVERY": 착불
	 * @return 주문키
	 * @throws IOException
	 */
	public String sendOrderInfoToNP(String xml) throws IOException {
//			String shippingType, String backURL, String nvadId) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
		/* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */ 
//		if (conn instanceof HttpsURLConnection) {
//			((HttpsURLConnection) conn).setSSLSocketFactory(_sslSockFactory);
//			((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
//				@Override
//				public boolean verify(String hostname, SSLSession session) {
//					return true;
//				}
//			});
//		}
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type", "application/xml; charset=utf-8");
		Writer writer = new OutputStreamWriter(conn.getOutputStream(), ENCODING);
//		writer.write(_makeQueryString(shopId, certificationKey, items, shippingPrice, shippingType, backURL));
		writer.write(xml);
		writer.flush();

		writer.close();
		int respCode = conn.getResponseCode();
		if (respCode != 200) {
			throw new RuntimeException(String.format("NP Response fail : %d %s", respCode, conn.getResponseMessage()));
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String orderKey = reader.readLine();
		return orderKey;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String send(String xml) throws Exception {

        HttpURLConnection con = null;
//		HttpsURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        String responseMsg;
        int responseCode;
        try{

            URL myUrl = new URL(_url.toString());
             con = (HttpsURLConnection)myUrl.openConnection();

            URL obj = new URL(_url.toString());
//            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.addRequestProperty("content-Type", "application/xml; charset=utf-8");


            con.setDoOutput(true);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(xml.getBytes("UTF-8"));
            wr.flush();

            responseMsg = con.getResponseMessage();
            responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){

                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String output;
                StringBuffer response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                    
                }
                in.close();

                //printing result from response
                
                responseMsg=response.toString();
                LOGGER.debug(response.toString());
            } else {
                LOGGER.debug(con.getResponseMessage());

            }


        }catch(Exception e){
            throw e;
        }finally {
            if(in != null) {
                try {
                    in.close();

                }catch(Exception e) {

              }
          }
          if(wr != null) {
              try {
                  wr.close();
              }catch(Exception e) {

              }
          }
          if(con != null) {
              try {
                  con.disconnect();
              }catch(Exception e) {

              }
          }
      }

        return responseMsg;

    }
	
	
	
	
	
	
	
	
	
	
	
	
	public String soapDataTransfer(String xml) {
		 
        try {
            URL url;
            /*
             * Weblogic에서 https url 사용하기 위한 방법 (new sun.net.www.protocol.https.Handler())
             */
            url = _url;
            
            /*
             * SSL 인증을 무조건 true로 만드는 소스 코드
             */
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() { 
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                    return null; } 

                public void checkClientTrusted(X509Certificate[] certs, String authType) { 
                } 

                public void checkServerTrusted(X509Certificate[] certs, String authType) { 

                } 
            } };

            SSLContext sc = SSLContext.getInstance("SSL"); 
            sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
//                 public boolean verify(String string,SSLSession ssls) {
//                        return true;
//                 }
//             }); 

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            /*
             * 연결 시간과 header 설정
             */
            
            conn.setConnectTimeout(3000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(xml);
            wr.flush();

            InputStreamReader in = new InputStreamReader(conn.getInputStream(),"utf-8");
            BufferedReader br = new BufferedReader(in);
            String strLine;
            String returnString = "";
            
            /*
             * 보낸 XML에 대한 응답을 받아옴
             */
            while ((strLine = br.readLine()) != null){
                returnString = returnString.concat(strLine);
            }

            in.close();
            wr.close();
            
            /*
             * 보낸 XML에 대한 응답을 출력.
             */
            System.out.println(returnString);

        } catch (Exception e) {
            e.printStackTrace();
        }        

        return "soapDataTransfer";
    }

	

//	private String getCookieValue(HttpServletRequest request, String name) {
//		if (name == null || request == null) {
//			return "";
//		}
//		Cookie[] cookies = request.getCookies();
//		if (cookies != null) {
//			for (int i = 0; i < cookies.length; i++) {
//				if (name.equals(cookies[i].getName())) {
//					return cookies[i].getValue();
//				}
//			}
//		}
//		return "";
//	}

	public String sendNpay(String url, String merchantId, String certiKey, String backUrl, UsitOrderTransaction orderTran) throws Exception
	{
	//주문 상품 내역으로 items 데이터를 생성한다.
//	List<ItemStack> items = new ArrayList<ItemStack>(); 
//	items.add(new ItemStack("a1", "아이템1", 2000, 1000, "", 3));
//	items.add(new ItemStack("a2", "아이템2", 2000, 1000, "XL/빨강", 2));
//	int shippingPrice = 2500;
//	String shippingType = "PAYED";
	//String backURL = "http://www.naver.com"; 
	//String nvadId="11111+aa12345678901234"; //CTS
	//servlet인 경우 쿠키값을 넣어야 함
	//String nvadId = getCookieValue((HttpServletRequest)request, "NVADID"); 
	//CPA스크립트 가이드 설치 업체는 해당 값 전달
	//String cpaInflowCode = getCookieValue((HttpServletRequest)request, "CPAValidator"); 
	//String naverInflowCode = getCookieValue((HttpServletRequest)request, "NA_CO");
//	NpayOrder sample = new NpayOrder("https://test-api.pay.naver.com/o/customer/api/order/v20/register");
	//NpayOrder sample = new NpayOrder("https://test-pay.naver.com/customer/api/order.nhn");
	String orderKey = "";//sample.sendOrderInfoToNC("naver_pay", "naver_pay",items.toArray(new ItemStack[0]), shippingPrice, shippingType, backUrl);
	//여기서 얻은 orderKey로 NC 결제창에 넘겨 결제를 진행한다.
//	System.out.println(String.format("OrderKey = %s", orderKey));
	setUrl(url);
	
	
	
	Document doc = new Document();  
	Element root = new Element(ORDER);
	Element merchantID = new Element(MERCHANTID);
	merchantID.setText(merchantId);
	Element certiKEY = new Element(CERTIKEY);
	certiKEY.setText(certiKey);
	
	//product는 orderItem만큼 생성
	for (UsitOrderItem orderItem : orderTran.getUsitOrderItems()) {
	int selectedCnt = 0;
	Element product = new Element(PRODUCT);
		Element productId = new Element(PRODUCT_ID);
		productId.setText(orderItem.getProductId().toString());
		product.addContent(productId);
		
		Element productName = new Element(PRODUCT_NAME);
		productName.setContent(new CDATA(orderItem.getProduct().getTitle()));
		product.addContent(productName);

		
		Element productBasePrice = new Element(PRODUCT_BASEPRICE);
		
		if("Y".equals(orderItem.getProduct().getDiscountYn())) {
			productBasePrice.setText(String.valueOf(orderItem.getProduct().getDiscountedPrice()));
		}else {
			productBasePrice.setText(String.valueOf((orderItem.getProduct().getPrice())));
		}
		product.addContent(productBasePrice);
		
		
		
		//세금유형보류
		Element taxType = new Element(PRODUCT_TAXTYPE);
		taxType.setText(orderItem.getProduct().getTaxStatus().getDetailCdNm());
		product.addContent(taxType);
		
		Element infoUrl = new Element(PRODUCT_INFOURL);
		infoUrl.setContent(new CDATA(PRODUCT_URL + orderItem.getProductId()));
		product.addContent(infoUrl);
		
		
		Element imageUrl = new Element(PRODUCT_IMGURL);
		imageUrl.setContent(new CDATA(orderItem.getProduct().getTitleImg()));//setText(orderItem.getProduct().getTitleImg());
		product.addContent(imageUrl);
		
	
		if(orderItem.getProductOptionId() == null) {
		Element single = new Element(PRODUCT_SINGLE);
			Element quantity = new Element(PRODUCT_SINGLE_QUANTITY);
			quantity.setText(String.valueOf(orderItem.getQuantity()));
		single.addContent(quantity);
		product.addContent(single);
		}else {
			
		Element option = new Element(OPTION);
		
			
			Element optionQuantity = new Element(OPTION_QUANTITY);
			optionQuantity.setText(String.valueOf(orderItem.getQuantity()));
			Element price = new Element(OPTION_PRICE);
			Element optionManageCode = new Element(OPTION_MANAGECODE);
			List<ProductOption> poList = orderItem.getProduct().getProductOptions();
			ProductOption orderItemOption = null;
			for (ProductOption productOption : poList) {
				if(productOption.getProductOptionId().equals(orderItem.getProductOptionId())) {
					orderItemOption = productOption;
					//seletedItem 갯수 구하기 우리의경우 optionvalue 수
					if(productOption.getOptionValue1() == null || productOption.getOptionValue2() == null) {
						selectedCnt = 1;
					}else {
						selectedCnt = 2;
					}
		    price.setText(String.valueOf(productOption.getAddPrice()));	
		    
		    
		    optionManageCode.setText(String.valueOf(orderItemOption.getProductOptionId()));
				}
			}
		option.addContent(optionQuantity);
		option.addContent(price);
		option.addContent(optionManageCode);
			
		
			for (int i = 0; i < selectedCnt; i++) {
			Element selectItem = new Element(OPTION_SELECTEDITEM);
				Element selectItemName = new Element(OPTION_SELECTEDITEM_NAME);
				//선택형:SELECT 입력형:INPUT
				Element selectItemType = new Element(OPTION_SELECTEDITEM_TYPE);
				selectItemType.setText(OPTION_SELECTEDITEM_TYPE_SELECT);
				Element selectItemValue = new Element(OPTION_SELECTEDITEM_VALUE);
					Element selectItemValueId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
					Element selectItemValueText = new Element(OPTION_SELECTEDITEM_VALUE_TEXT);	
			if(i==0) {
				selectItemName.setText(orderItemOption.getOptionName1());
				selectItemValueId.setText(String.valueOf(orderItemOption.getProductOptionId()));
				selectItemValueText.setText(orderItemOption.getOptionValue1());
			}else {
				selectItemName.setText(orderItemOption.getOptionName2());
				selectItemValueId.setText(String.valueOf(orderItemOption.getProductOptionId()));
				selectItemValueText.setText(orderItemOption.getOptionValue2());
			}
			
				selectItemValue.addContent(selectItemValueId);
				selectItemValue.addContent(selectItemValueText);
			selectItem.addContent(selectItemName);
			selectItem.addContent(selectItemType);
			selectItem.addContent(selectItemValue);
			option.addContent(selectItem);
			}
	
		Element shippingPoilcy = new Element(SHIPPINGPOLICY);
			Element gruopId = new Element(SHIPPINGPOLICY_GRUOPID);
			gruopId.setText(String.valueOf(orderItem.getDeliveryFeeId()));
			Element method = new Element(SHIPPINGPOLICY_METHOD);
			method.setText(SHIPPINGPOLICY_METHOD_DELIVERY);
			
			Element feeType = new Element(SHIPPINGPOLICY_FEETYPE);
			Element feePayType = new Element(SHIPPINGPOLICY_FEEPAYTYPE);
			Element feePrice = new Element(SHIPPINGPOLICY_FEEPRICE);
			
			DeliveryFee orderDeliveryFee = null;
			
			for (DeliveryFee deliveryFee : orderTran.getDeliveryFees()) {
				if(deliveryFee.getDeliveryFeeId().equals(orderItem.getDeliveryFeeId())) {
//					orderDeliveryFee
					orderDeliveryFee = deliveryFee;
				}
				
			}
			
			if(orderDeliveryFee.getAmount() == 0) {
			feeType.setText(SHIPPINGPOLICY_FEETYPE_FREE);
			feePayType.setText(SHIPPINGPOLICY_FEETYPE_FREE);
			feePrice.setText("0");

			}else {
			feeType.setText(SHIPPINGPOLICY_FEETYPE_CHARGE);
			feePayType.setText(SHIPPINGPOLICY_FEEPAYTYPE_PREPAYED);
			feePrice.setText(String.valueOf(orderDeliveryFee.getAmount()));
			}
			shippingPoilcy.addContent(gruopId);
			shippingPoilcy.addContent(method);
			shippingPoilcy.addContent(feeType);
			shippingPoilcy.addContent(feePayType);
			shippingPoilcy.addContent(feePrice);
			
			
			
//			Element conditionalFree = new Element(SHIPPINGPOLICY_CONDITIONALFREE);
//				Element basePrice = new Element(SHIPPINGPOLICY_CONDITIONALFREE_BASEPRICE);
		product.addContent(option);
		product.addContent(shippingPoilcy);
			
			
		}
		root.addContent(product);//root element 의 하위 element 를 만들기
	}
	Element backURL = new Element(BACKURL);
	backURL.setContent(new CDATA(backUrl));//.setText(backUrl);
	root.addContent(merchantID);
	root.addContent(certiKEY);
	root.addContent(backURL);
	
	doc.setRootElement(root);
	//String 으로 xml 출력
     XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8")) ;
     System.out.println(outputter.outputString(doc));

//     orderKey = sendOrderInfoToNP(outputter.outputString(doc));
     orderKey = send(outputter.outputString(doc));
//     orderKey = soapDataTransfer(outputter.outputString(doc));
     System.out.println(orderKey);
     
	return orderKey; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 상품정보제공 API
	 * @param url
	 * @param merchantId
	 * @param certiKey
	 * @param backUrl
	 * @param orderTran
	 * @return
	 * @throws Exception
	 */
	public String generateNpayProduct(ArrayList<Product> list,HashMap<Integer, String> map,boolean supplementSearch, boolean optionSearch) throws Exception
	{

		
		
		
		
//		String orderKey = "";// sample.sendOrderInfoToNC("naver_pay", "naver_pay",items.toArray(new
								// ItemStack[0]), shippingPrice, shippingType, backUrl);
		Document doc = new Document();
		Element root = new Element(PRODUCTS);
//		Element merchantID = new Element(MERCHANTID);
//		Element certiKEY = new Element(CERTIKEY);

		// products는 요청상품 ID만큼 생성
		for (Product products : list) {
			String optionManageCodes = map.get(products.getProductId());
//			int selectedCnt = 0;
			Element product = new Element(PRODUCT);
				Element productId = new Element(PRODUCT_ID);
				productId.setText(String.valueOf(products.getProductId()));
			product.addContent(productId);

				Element productName = new Element(PRODUCT_NAME);
				productName.setContent(new CDATA(products.getTitle()));
			product.addContent(productName);

				Element productBasePrice = new Element(PRODUCT_BASEPRICE);

				if ("Y".equals(products.getDiscountYn())) {
				productBasePrice.setText(String.valueOf(products.getDiscountedPrice()));
				} else {
				productBasePrice.setText(String.valueOf((products.getPrice())));
				}
			product.addContent(productBasePrice);

				// 세금유형보류
				Element taxType = new Element(PRODUCT_TAXTYPE);
				taxType.setText(products.getTaxStatus().getDetailCdNm());
			product.addContent(taxType);

				Element infoUrl = new Element(PRODUCT_INFOURL);
				infoUrl.setContent(new CDATA(PRODUCT_URL + products.getProductId()));
			product.addContent(infoUrl);

				Element imageUrl = new Element(PRODUCT_IMGURL);
				imageUrl.setContent(new CDATA(products.getTitleImg()));// setText(orderItem.getProduct().getTitleImg());
			product.addContent(imageUrl);

			
				Element returnShippingFee = new Element(PRODUCT_RETURN_SHIPPING_FEE);
				returnShippingFee.setText(String.valueOf(products.getDeliveryPrice()));
			product.addContent(returnShippingFee);
				Element exchangeShippingFee = new Element(PRODUCT_EXCHANGE_SHIPPING_FEE);
				exchangeShippingFee.setText(String.valueOf(products.getDeliveryPrice()*2));
			product.addContent(exchangeShippingFee);
			
				Element returnInfo = new Element(PRODUCT_RETURN_INFO);
					Element zipcode = new Element(RETURN_INFO_ZIPCODE);
					zipcode.setText(products.getSellMember().getReturnPostcode());
				returnInfo.addContent(zipcode);
					Element address1 = new Element(RETURN_INFO_ADDRESS1);
					address1.setText(products.getSellMember().getReleaseAddress());
				returnInfo.addContent(address1);	
					Element address2 = new Element(RETURN_INFO_ADDRESS2);
					address2.setText(products.getSellMember().getReturnAddressDetail());
				returnInfo.addContent(address2);	
					Element sellername = new Element(RETURN_INFO_SHELLERNAME);
					sellername.setText(products.getSellMember().getName());
				returnInfo.addContent(sellername);	
					Element contact1 = new Element(RETUNR_INFO_CONTACT1);
					contact1.setText(products.getSellMember().getReturnPhone());
				returnInfo.addContent(contact1);
			product.addContent(returnInfo);
			
			
			if ("N".equals(products.getOptionUseYn())) {
				Element stockQuantity = new Element(PRODUCT_STOCK_QUANTITY);
				stockQuantity.setText(String.valueOf(products.getInventory()));
			product.addContent(stockQuantity);
			} else {

				//optionSearch=true 이면 optionManageCodes포함되는것만
//				Element option = new Element(OPTION);
//					Element optionItem = new Element(OPTION_OPTIONITEM);
//						Element optionItemType = new Element(OPTION_SELECTEDITEM_TYPE);
//						Element optionItemName = new Element(OPTION_SELECTEDITEM_NAME);
//						Element optionItemValue = new Element(OPTION_SELECTEDITEM_VALUE);
//							Element optionItemValueId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
//							Element optionItemValueText = new Element(OPTION_SELECTEDITEM_VALUE_TEXT);
							

//				Element optionQuantity = new Element(OPTION_QUANTITY);
//				optionQuantity.setText(String.valueOf(orderItem.getQuantity()));
				
				
				
				Element option = new Element(OPTION);
				boolean isSecondItem = false;
				List<ProductOption> poList = products.getProductOptions();
				int potion = poList.size();
				ProductOption orderItemOption = null;
					Element optionItem = new Element(OPTION_OPTIONITEM);
					Element optionItemSec = new Element(OPTION_OPTIONITEM);
				
				for (int i = 0; i < potion; i++) {
					if (i == 0) {
				//optionSearch=true 이면 optionManageCodes포함되는것만
						if(poList.get(i).getOptionValue2() != null) {
							isSecondItem = true;
						}
				
					
						Element optionItemType = new Element(OPTION_SELECTEDITEM_TYPE);
						optionItemType.setText(OPTION_SELECTEDITEM_TYPE_SELECT);
						Element optionItemTypeSec = new Element(OPTION_SELECTEDITEM_TYPE);
						optionItemTypeSec.setText(OPTION_SELECTEDITEM_TYPE_SELECT);
						
						Element optionItemName = new Element(OPTION_SELECTEDITEM_NAME);
						optionItemName.setContent(new CDATA(poList.get(i).getOptionName1()));
//						optionItemName.setText(poList.get(i).getOptionName1());

						Element optionItemNameSec = new Element(OPTION_SELECTEDITEM_NAME);
						optionItemNameSec.setContent(new CDATA(poList.get(i).getOptionName2()));
//						optionItemNameSec.setText(poList.get(i).getOptionName2());
						
					optionItem.addContent(optionItemType);
					optionItem.addContent(optionItemName);
					
					optionItemSec.addContent(optionItemTypeSec);
					optionItemSec.addContent(optionItemNameSec);
					}
						
						Element optionItemValue = new Element(OPTION_SELECTEDITEM_VALUE);
							Element optionItemValueId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
							optionItemValueId.setText(String.valueOf(poList.get(i).getProductOptionId()));
							Element optionItemValueText = new Element(OPTION_SELECTEDITEM_VALUE_TEXT);
							optionItemValueText.setContent(new CDATA(poList.get(i).getOptionValue1()));
//							optionItemValueText.setText(poList.get(i).getOptionValue1());
						optionItemValue.addContent(optionItemValueId);
						optionItemValue.addContent(optionItemValueText);
							
						Element optionItemValueSec = new Element(OPTION_SELECTEDITEM_VALUE);
							Element optionItemValueIdSec = new Element(OPTION_SELECTEDITEM_VALUE_ID);
							optionItemValueIdSec.setText(String.valueOf(poList.get(i).getProductOptionId()));
							Element optionItemValueTextSec = new Element(OPTION_SELECTEDITEM_VALUE_TEXT);
							optionItemValueTextSec.setContent(new CDATA(poList.get(i).getOptionValue2()));
//							optionItemValueTextSec.setText(poList.get(i).getOptionValue2());
						optionItemValueSec.addContent(optionItemValueIdSec);
						optionItemValueSec.addContent(optionItemValueTextSec);
							
							
						
					optionItem.addContent(optionItemValue);
					optionItemSec.addContent(optionItemValueSec);
						
						
						
						
						
//						orderItemOption = productOption;
						// seletedItem 갯수 구하기 우리의경우 optionvalue 수
//						if (productOption.getOptionValue1() == null || productOption.getOptionValue2() == null) {
//							selectedCnt = 1;
//						} else {
//							selectedCnt = 2;
//						}
//						price.setText(String.valueOf(productOption.getAddPrice()));

//						optionManageCode.setText(String.valueOf(orderItemOption.getProductOptionId()));
					}
				
				if(isSecondItem) {
				option.addContent(optionItem);
				option.addContent(optionItemSec);
				}else {
				option.addContent(optionItem);
				}
				
				
				
				
				
				//전부노출시키는 경우
				if(optionSearch && (optionManageCodes == null || optionManageCodes =="") ) {
					
					for (int i = 0; i < potion; i++) {
						Element combination = new Element(OPTION_COMBINATION);
							Element combinationManageCode = new Element(OPTION_COMBINATION_MANAGECODE);
							combinationManageCode.setText(String.valueOf(poList.get(i).getProductOptionId()));
						combination.addContent(combinationManageCode);
							Element combinationPrice = new Element(OPTION_PRICE);
							combinationPrice.setText(String.valueOf(poList.get(i).getAddPrice()));
						combination.addContent(combinationPrice);
						if (isSecondItem) {
							
							Element combinationOptions = new Element(OPTION_COMBINATION_OPTIONS);
								Element combinationOptionsName = new Element(OPTION_SELECTEDITEM_NAME);
								combinationOptionsName.setContent(new CDATA(poList.get(i).getOptionName1()));
//								combinationOptionsName.setText(poList.get(i).getOptionName1());
								Element combinationOptionsId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
								combinationOptionsId.setText(String.valueOf(poList.get(i).getProductOptionId()));
							
							Element combinationOptionsSec = new Element(OPTION_COMBINATION_OPTIONS);
								Element combinationOptionsNameSec = new Element(OPTION_SELECTEDITEM_NAME);
								combinationOptionsNameSec.setContent(new CDATA(poList.get(i).getOptionName2()));
//								combinationOptionsNameSec.setText(poList.get(i).getOptionName2());
								Element combinationOptionsIdSec = new Element(OPTION_SELECTEDITEM_VALUE_ID);
								combinationOptionsIdSec.setText(String.valueOf(poList.get(i).getProductOptionId()));
							
							combinationOptions.addContent(combinationOptionsId);
							combinationOptions.addContent(combinationOptionsName);
						combination.addContent(combinationOptions);
							
							combinationOptionsSec.addContent(combinationOptionsNameSec);
							combinationOptionsSec.addContent(combinationOptionsIdSec);
						combination.addContent(combinationOptionsSec);
					option.addContent(combination);
						} else {
							Element combinationOptions = new Element(OPTION_COMBINATION_OPTIONS);
								Element combinationOptionsName = new Element(OPTION_SELECTEDITEM_NAME);
								combinationOptionsName.setContent(new CDATA(poList.get(i).getOptionName1()));
//								combinationOptionsName.setText(poList.get(i).getOptionName1());
								Element combinationOptionsId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
								combinationOptionsId.setText(String.valueOf(poList.get(i).getProductOptionId()));
						
							
							combinationOptions.addContent(combinationOptionsId);
							combinationOptions.addContent(combinationOptionsName);
						combination.addContent(combinationOptions);
					option.addContent(combination);
						
						}

						
					}
				product.addContent(option);
				//optionManageCodes 가 있을 경우
				}else if (optionSearch && (optionManageCodes.split(",").length > 0) ) {
					String [] omc = optionManageCodes.split(",");
					int loop = omc.length;
					for (int i = 0; i < potion; i++) {
						
						
						for (int j = 0; j < loop; j++) {
						
							if(omc[j].equals(String.valueOf(poList.get(i).getProductOptionId()))) {
						
						Element combination = new Element(OPTION_COMBINATION);
							Element combinationManageCode = new Element(OPTION_COMBINATION_MANAGECODE);
							combinationManageCode.setText(String.valueOf(poList.get(i).getProductOptionId()));
						combination.addContent(combinationManageCode);
							Element combinationPrice = new Element(OPTION_PRICE);
							combinationPrice.setText(String.valueOf(poList.get(i).getAddPrice()));
						combination.addContent(combinationPrice);
						if (isSecondItem) {
							
							Element combinationOptions = new Element(OPTION_COMBINATION_OPTIONS);
								Element combinationOptionsName = new Element(OPTION_SELECTEDITEM_NAME);
								combinationOptionsName.setContent(new CDATA(poList.get(i).getOptionName1()));
//								combinationOptionsName.setText(poList.get(i).getOptionName1());
								Element combinationOptionsId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
								combinationOptionsId.setText(String.valueOf(poList.get(i).getProductOptionId()));
							
							Element combinationOptionsSec = new Element(OPTION_COMBINATION_OPTIONS);
								Element combinationOptionsNameSec = new Element(OPTION_SELECTEDITEM_NAME);
								combinationOptionsNameSec.setContent(new CDATA(poList.get(i).getOptionName2()));
//								combinationOptionsNameSec.setText(poList.get(i).getOptionName2());
								Element combinationOptionsIdSec = new Element(OPTION_SELECTEDITEM_VALUE_ID);
								combinationOptionsIdSec.setText(String.valueOf(poList.get(i).getProductOptionId()));
								
							combinationOptions.addContent(combinationOptionsId);
							combinationOptions.addContent(combinationOptionsName);
							
							combinationOptionsSec.addContent(combinationOptionsIdSec);
							combinationOptionsSec.addContent(combinationOptionsNameSec);
							
						combination.addContent(combinationOptions);
						combination.addContent(combinationOptionsSec);	
					option.addContent(combination);
						} else {
							Element combinationOptions = new Element(OPTION_COMBINATION_OPTIONS);
								Element combinationOptionsName = new Element(OPTION_SELECTEDITEM_NAME);
								combinationOptionsName.setContent(new CDATA(poList.get(i).getOptionName1()));
//								combinationOptionsName.setText(poList.get(i).getOptionName1());
								Element combinationOptionsId = new Element(OPTION_SELECTEDITEM_VALUE_ID);
								combinationOptionsId.setText(String.valueOf(poList.get(i).getProductOptionId()));
						
							
							combinationOptions.addContent(combinationOptionsId);
							combinationOptions.addContent(combinationOptionsName);
						combination.addContent(combinationOptions);
					option.addContent(combination);
						
						}

						}
					}
					
						
					
					
					}
				product.addContent(option);
					
					
				}	
				
				
				}


				Element shippingPoilcy = new Element(SHIPPINGPOLICY);
//				Element gruopId = new Element(SHIPPINGPOLICY_GRUOPID);
//				gruopId.setText(String.valueOf(orderItem.getDeliveryFeeId()));
					Element method = new Element(SHIPPINGPOLICY_METHOD);
					method.setText(SHIPPINGPOLICY_METHOD_DELIVERY);
	
					Element feeType = new Element(SHIPPINGPOLICY_FEETYPE);
					feeType.setText(SHIPPINGPOLICY_FEETYPE_CONDITIONAL_FREE);
					
					Element feePayType = new Element(SHIPPINGPOLICY_FEEPAYTYPE);
					feePayType.setText(SHIPPINGPOLICY_FEEPAYTYPE_PREPAYED);
					
					Element feePrice = new Element(SHIPPINGPOLICY_FEEPRICE);
					feePrice.setText(String.valueOf(products.getDeliveryPrice()));
					
					
					Element conditionalFree = new Element(SHIPPINGPOLICY_CONDITIONALFREE);
					
						Element basePrice = new Element(SHIPPINGPOLICY_CONDITIONALFREE_BASEPRICE);
						basePrice.setText(String.valueOf(products.getDeliveryPriceCut()));
					conditionalFree.addContent(basePrice);
				shippingPoilcy.addContent(method);
				shippingPoilcy.addContent(feeType);
				shippingPoilcy.addContent(feePayType);
				shippingPoilcy.addContent(feePrice);
				shippingPoilcy.addContent(conditionalFree);
				
				product.addContent(shippingPoilcy);

			root.addContent(product);// root element 의 하위 element 를 만들기
			}
			
			
		doc.setRootElement(root);
		// String 으로 xml 출력
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
		System.out.println(outputter.outputString(doc));

		// orderKey = sendOrderInfoToNP(outputter.outputString(doc));
		// orderKey = send(outputter.outputString(doc));
		// orderKey = soapDataTransfer(outputter.outputString(doc));
		return outputter.outputString(doc);
			
			
		}
	
	
	
	
}