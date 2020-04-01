package com.why.bean.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:db/public.properties"})
public class PublicInfo {
	@Value("${shop}")
	public String shopid;
	@Value("${userid}")
	public String userid;
	@Value("${centrally}")
	public String centrally;
	public String getCentrally() {
		return centrally;
	}

	public void setCentrally(String centrally) {
		this.centrally = centrally;
	}

	public String getUserid() {
		return userid;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
 
}
