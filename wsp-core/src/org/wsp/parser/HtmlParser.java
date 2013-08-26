package org.wsp.parser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;

public class HtmlParser {
	private static final Logger log = Logger.getLogger(HtmlParser.class);
	private String Url;
	private Turbo turbo;

	public HtmlParser(String url) {
		super();
		Url = url;
	}

	public HtmlParser() {
		super();
	}

	public HtmlParser(Turbo turbo) {
		super();
		Url = turbo.getUrl();
		this.turbo = turbo;
	}

	private List<String> ParsingTurbo() {
		System.setProperty("sun.net.client.defaultReadTimeout", "5000");
		System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
		int Size = 0;
		String[] htmls = null;
		List<String> HtmlEx = new ArrayList<String>();
		int loop = 0;

		StringExtractor se = new StringExtractor(Url);
		String html;
		html = se.extractStrings(false);
		htmls = html.split("\n");
		Size = htmls.length;
		if (Size > 360) {
			log.debug("Page Content : " + html);
			for (int j = 0; j < htmls.length; j++) {
				HtmlEx.add(j, htmls[j]);
				log.debug(j + "**" + htmls[j]);
			}

		} else {
			log.error("Problem with parsing the Turbo Page, Size = " + Size);
		}
		return HtmlEx;
	}

	public TurboPosition parsePosition() {

		Float Index = new Float(0);
		List<String> HtmlEx = ParsingTurbo();
		try {
			if (HtmlEx == null) {
				return null;
			}
			if (HtmlEx.indexOf("Direct Emetteurs Temps réel") == -1) {
				log.error("Can not get the CAC 40 Value....");
				Index = new Float(0);
			} else {
				String tmps = HtmlEx.get(HtmlEx
						.indexOf("Direct Emetteurs Temps réel") + 2);
				String[] tmp = tmps.split(" ");
				for (int i = 0; i < tmp.length; i++) {
					log.debug(tmp[i]);
				}
				try {
					Index = new Float(tmp[7] + tmp[8]);
				} catch (NumberFormatException e) {
					Index = new Float(tmp[8] + tmp[9]);
				}
			}
			Integer Qte = 0;
			float Achat = 0;
			float Vente = 0;
			if (HtmlEx.indexOf("Bid and Ask") != -1) {
				log.info("Getting the Turbo Value from the Ask and Bid");

				Qte = new Integer(HtmlEx.get(HtmlEx.indexOf("Bid and Ask") + 5)
						.replaceAll(" ", ""));
				if (HtmlEx.get(HtmlEx.indexOf("Bid and Ask") + 6)
						.equalsIgnoreCase("OUV")) {
					log.error("Turbo Inactif...");
					return null;
				} else {
					Achat = new Float(
							HtmlEx.get(HtmlEx.indexOf("Bid and Ask") + 6))
							.floatValue();
					log.info(Achat);
				}
				if (HtmlEx.get(HtmlEx.indexOf("Bid and Ask") + 9)
						.equalsIgnoreCase("OUV")) {
					log.error("Turbo Inactif...");
					return null;
				} else {
					Vente = new Float(
							HtmlEx.get(HtmlEx.indexOf("Bid and Ask") + 9))
							.floatValue();
					log.info(Vente);
				}

			} else {
				log.info("Getting The Turbo Value from the Cotations");
				if (HtmlEx.get(HtmlEx.indexOf("Cotations") + 2).contains("c")) {
					log.error("Turbo Closed");
					return null;
				} else {
					Achat = (float) (new Float(HtmlEx.get(
							HtmlEx.indexOf("Cotations") + 2).split(" ")[0])
							.floatValue() - 0.015);
					Vente = (float) (new Float(HtmlEx.get(
							HtmlEx.indexOf("Cotations") + 2).split(" ")[0])
							.floatValue() + 0.015);
				}

			}
			if (Achat == 0) {
				return null;

			} else {
				TurboPosition position = new TurboPosition();
				position.setAchat(Achat);
				//position.setTurboIdTurbo(0);
				position.setTurboIdTurbo(turbo.getIdTurbo());
				position.setPrixSousJacent(Index);
				position.setQte(Qte);
				position.setCreationDate(new Date());
				position.setTradingSessionIdTradingSession(0);
				position.setTradingSessionIdTradingSession(turbo
				 .getTradingSessionIdTradingSession());
				position.setVente(Vente);
				// System.out.println(position);
				log.info("Position Parsed : " + position);
				return position;
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			log.error("Problem with parsing the Turbo page!!!!");
			return null;
		}

	}

	public Turbo parseTurbo() {
		List<String> HtmlEx = ParsingTurbo();
		if (HtmlEx.indexOf("Caractéristiques") != -1) {
			int i = HtmlEx.indexOf("Caractéristiques");
			String HN = HtmlEx.get(i + 2).trim();
			String HNs[] = HN.split("/");
			SimpleDateFormat Sdf = new SimpleDateFormat("HH:mm:ss");
			Date Hd;
			try {
				Hd = Sdf.parse(HNs[0].trim());

				Date HF = Sdf.parse(HNs[1].trim());
				String Code = HtmlEx.get(i - 45).trim().replaceAll(" ", "");
				String Name = HtmlEx.get(i - 44).trim().split(" ")[0];
				String Type = HtmlEx.get(i + 6).trim();
				Float BD = Float.valueOf(HtmlEx.get(i + 8).trim()
						.replaceAll(" ", "").replace(',', '.'));
				Sdf = new SimpleDateFormat("dd/MM/yyyy");
				String Em = "";
				Date DEm = new Date();
				Date DEc = new Date();
				;
				if (HtmlEx.get(i + 12).trim().equalsIgnoreCase("Parité")) {
					return null;
				} else {
					DEc = Sdf.parse(HtmlEx.get(i + 12).trim());
					Em = HtmlEx.get(i + 20).trim();
					DEm = Sdf.parse(HtmlEx.get(i + 22).trim());
				}
				String SJ = HtmlEx.get(i + 4).trim();

				turbo = new Turbo();
				turbo.setBarDes(BD);
				turbo.setBeginNeg(Hd);
				turbo.setCode(Code);
				turbo.setEcheanceDate(DEc);
				turbo.setEmetteur(Em);
				turbo.setEmissionDate(DEm);
				turbo.setEndNed(HF);
				turbo.setName(Name);
				turbo.setSousJacant(SJ);
				turbo.setState(1);
				turbo.setTypeTurbo(Type);
				turbo.setCreationDate(new Date());
				turbo.setUrl(Url);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
				return null;
			}
		} else {
			log.error("Problem Parsing Turbo : " + this.Url);
			return null;
		}
		return turbo;

	}

	public List<Turbo> getAllTurbo(String type, String sort) {
		ArrayList<Turbo> Alturbo = new ArrayList<>();
		try {
			URL u = new URL("http://www.boursorama.com/ajax/ui/refresh.phtml");
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("POST");
			c.setRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64; rv:23.0) Gecko/20100101 Firefox/23.0");
			c.setRequestProperty("Accept", "application/json");
			c.setRequestProperty("Accept-Language",
					"fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
			// c.setRequestProperty("Accept-Encoding", "gzip, deflate");
			c.setRequestProperty("Content-length", "215");
			c.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			c.setRequestProperty("X-Request", "JSON");
			c.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			// c.setUseCaches(false);
			// c.setAllowUserInteraction(false);
			c.setRequestProperty("Referer",
					"http://www.boursorama.com/bourse/derives/turbos/");
			String Cookies = "OBJECT_BOURSORAMA=0; __utma=88976520.461628468.1376484469.1376484469.1376645564.2; __utmz=88976520.1376484469.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __utma=88976520.461628468.1376484469.1376484469.1376645564.2; __utmz=88976520.1376484469.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); xtvrn=$513673$; xtan=-; xtant=1; TestIfCookieP=ok; pbw=%24b%3D12230%3B%24o%3D99999%3B%24sh%3D768%3B%24sw%3D1280; pid=7719877355728471559; pdomid=7; pbwmaj6=y; PHPSESSIONID=b952c69ebc9e2ef77d65c26801866615; STORAGE_COOKIE=5lw7cYx1vs1gVmEBMqWjem0D%2BPN9A0iLfhG5LdGOtCd46ihy7ftxpPJCNtyzFZMasMPTzAmeDBsWf6FtN0%2Bd6VDvR%2F5DHHyauGR5EGyWgBr0vTs1jk4c3LCbqIIuoHnsRvr9Wig7uKgdEflvvHJT9tM7YtqfFdTxqrDp24ppd4tPBJ9r%2Fxfd4114hsZ0%2F3PcGDXe0vN0xywoTm5rZw2MOg0sFgPneD04lnKwge5PyWDba5f7JmbQVs0D2I2ushNEbwvt3N9kneR1f9t3pE1SoKBU9i%2BqItfFPnIaSaoLDydGskrm2IX%2BHK7pBhMhyKV5k0iKYDGZxzdW8KjgjJREaZkWcHdFWWKU2vfogoIUHqVLBbtiAgzeUSlb5BsUoJMB1JZ89EWe%2F1KsI7bMcIz6K0NJ2wYkzvP0o8nEI8n0Qu148lEoc%2BY7J7JZAxjFtwOS%2FMHNXXH8xN5372BNDw9QlbDKIr04%2B3jFvxpB7dy5zNIUKpMh4V2yp1ZxWbLCy5ma3WZL2Ieha4A9A7O8gMQjal1oPCivtYbDIra2FTBdzeF7fzQWKFc7txM8IDjPfkBPk7CrwY4%2FGW1JPLxCS6%2BIGtczeo%2FadcHgE%2Ft7JUPxRCOxgXFYpOhiQIRrXA%2FIXluK93e4Ka0%2F0e%2BnEQj8XaT%2B8GVxOcvfjuN7c7Fx6uHar9n18XhJyZVQgYUT%2BQl1NDzpXwJjBIOyYlo1Gnu7895cl%2FUcTojhrfVOOFlHBYxgCnpp1kl%2B1FirBcInyCeo9Rs24uV6hPPAHdcy2heJnF6J2luS4bqUVp2lmeKXtC0%2FN96iZFzUVAYnv0yi1HZcDz4tGpkybuYhpYSMW09claGN%2FIP0lJB2ZaoKciLwrN4TzP0DCt9Tz%2F5W9lXBIpGXCGOlMkXFUn8RgqoQT63gfXu5WGakJzda%2BYNic51VVLCbMlrmpnPJmERcuRwEBFqteRZAqHZ%2Fum6ulFGQpZaQXMXK5R8JkBSgh77q%2F%2Fx7phNqQlodoO9lI%2B0VINt%2FyyqVvyN91hBuaC5SNyhH%2B1DzYLu9FiJbhhNzJPaYsqpJML%2BM1rGOcCXgtGSUemxI3digqCni7Y5Viu%2BnLHwtAphfp9ZTiPtPSLSPqhS93WLmrQA0OXFnDe9v7BnPLEjAyKUYNr1s9Dy40rlZplbY1SxjEtuHQMskPeCP8UFcK2qc6EMkKt6clUAEp%2FrXkO3XSM7MQhC3dwLC6sN%2BRRKnZsGzwbBaTWvK%2FWdjIZn174mlgA4%2BE5KtJ7xgnLsBG3Ho8isJ3qGQSu%2BTzQWmITkiwZkwqNRP6W0BE1QTRiXe8zXtgTxjjfyzTBm8%2BhpR6abnaGZewheUn9hZz3wyphBkT%2FQVcMfZNyjexPZugr9%2FTKQXBD6Se%2BNsUmDi6i8dZWgk0Bsne8j2KDGRRezlz3zX1%2BXzR%2Bk13Xt9xD7nWj9BP16RyG4OxQ4yTBtkMcjfI7LgWmmDjuz%2FwgtCoS6UzDMuSqwWfhs8QdzNDYkrDnJRdnhENGi7%2F20%3D; __utmb=88976520.0.10.1376645564; __utmc=88976520; __utmb=88976520.1.10.1376645564; __utmc=88976520; vs=26616=5587939; sasd2=q=%24qc%3d1312746033%3b%24ql%3dunknown%3b%24qpc%3d1000%3b%24qpp%3d0%3b%24qt%3d233_785_25284t&c=1&a=-77932950&l=&lo=&lt=635122495698513555; sasd=%24qc%3d1312746033%3b%24ql%3dunknown%3b%24qpc%3d1000%3b%24qpp%3d0%3b%24qt%3d233_785_25284t; profilInfos_width=1353; dyncdn=limit";
			c.setRequestProperty("Cookie", Cookies);
			c.setDoInput(true);
			c.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(c.getOutputStream());
			String query = "parameters[sort]="+sort+"&parameters[nbLines]=50&parameters[ssjacentSymbol]=false&parameters[type_derive]=C&parameters[risque]=false&parameters[turbo]=true&parameters[page]=1&parameters[do_search]=1&parameters[type]="+type+"&parameters[type_ssjacent]=Indices&parameters[ssjacent]=CAC%252040&parameters[emetteur]=&parameters[status][active]=active&parameters[ssjacent-part]=&parameters[strategie]=&parameters[eligibilite]=CATS&customizing=true&id=b82e2031&config[id]=b82e2031&config[fieldsLast][0][0]=type&config[newFields][0]=type&class=Boursorama_Block_Bourse_Derives_Search_Turbos";
			wr.writeBytes(query);
			wr.flush();
			wr.close();
			c.setConnectTimeout(999);
			c.setReadTimeout(999);
			c.connect();
			int status = c.getResponseCode();
			// System.out.println(status);
			if (status == 200 || status == 201) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						c.getInputStream()));

				StringBuilder sb = new StringBuilder();
				String line;
				// System.out.println(br.readLine());
				while ((line = br.readLine()) != null) {
					sb.append(line);
					// br.close();
				}

				// System.out.println(sb.toString());
				String res = sb.toString();
				log.info(res);

				String[] tmp = res.split("<a href");
				for (int i = 1; i < tmp.length; i++) {

					String fin = "http://www.boursorama.com/"
							+ tmp[i].substring(5, 40);
					// System.out.println(tmp[i]);
					log.info(fin);
					Url = fin;
					Alturbo.add(parseTurbo());
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Alturbo;
	}

}
