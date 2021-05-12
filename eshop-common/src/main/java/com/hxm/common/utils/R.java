package com.hxm.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpStatus;

import java.util.*;

/**
 * 返回数据
 *
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("msg", "success");
	}

	// 利用fastjson进行反序列化
	public <T> T getData(TypeReference<T> typeReference) {
		// 默认是map
		Object data = get("data");
		String jsonString = JSON.toJSONString(data);
		return JSON.parseObject(jsonString, typeReference);
	}

	// 利用fastjson进行反序列化
	public <T> T getData(String key, TypeReference<T> typeReference) {
		// 默认是map
		Object data = get(key);
		String jsonString = JSON.toJSONString(data);
		return JSON.parseObject(jsonString, typeReference);
	}
	
	public static R error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public boolean wordBreak(String s, List<String> wordDict) {
		boolean[] dp =new boolean[s.length()];
		Set<String> set = new HashSet<>(wordDict);
		set.add("");
		for(int limit=0; limit<s.length(); limit++){
			for(int i=limit; i>=0; i--){
				dp[i]=dp[i] || set.contains(s.substring(0,i+1));
				if(dp[i] && set.contains(s.substring(i+1,limit+1))){
					dp[limit]=true;
					break;
				}
			}
		}
		System.out.println(JSONObject.toJSONString(dp));
		return dp[s.length()-1];
	}

	public static void main(String[] args) {
		String s="applepenapple";
		String [] sarray={"apple","pen"};
		List<String> list= Arrays.asList(sarray);
		R r=new R();
		System.out.println(r.wordBreak(s, list));
	}

	public  Integer getCode() {
		return (Integer) this.get("code");
	}
}
