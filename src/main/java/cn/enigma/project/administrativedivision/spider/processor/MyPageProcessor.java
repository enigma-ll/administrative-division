package cn.enigma.project.administrativedivision.spider.processor;

import cn.enigma.project.administrativedivision.dto.AreaDTO;
import cn.enigma.project.administrativedivision.global.Globals;
import cn.enigma.project.administrativedivision.spider.DownLoadBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luzh
 * Create: 2019-07-01 10:33
 * Modified By:
 * Description:
 */
public class MyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(5).setSleepTime(3000).setUserAgent(Globals.USERAGENT).setCharset("GBK").setTimeOut(100000);

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        url = url.endsWith("html") ? url : url.endsWith(File.separator) ? url : url + File.separator;
        String baseUrl = url.substring(0, url.lastIndexOf("/")) + "/";
        String parentCode = getCodeFromUrl(url);
        Document document = Jsoup.parse(page.getHtml().get());
        Elements tables = document.select("table");
        List<AreaDTO> list = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        for (Element table : tables) {
            if (table.hasAttr("class") && table.attr("class").contains("table")) {
                // 首页省份信息
                if (table.attr("class").equals("provincetable")) {
                    parentCode = "86";
                    Elements trs = table.select("tr.provincetr");
                    for (Element tr : trs) {
                        Elements tds = tr.select("td");
                        for (Element td : tds) {
                            Elements as = td.select("a");
                            list.add(new AreaDTO(
                                    as.get(0).text(),
                                    getCodeFromUrl(as.get(0).attr("href")),
                                    getCodeFromUrl(as.get(0).attr("href")),
                                    parentCode,
                                    url + as.get(0).attr("href")));
                            urlList.add(url
                                    + getCodeFromUrl(as.get(0).attr("href")) + ".html");
                        }
                    }
                } else if (table.attr("class").equals("villagetable")) { // 最底层区域结构
                    Elements trs = table.select("tr.villagetr");
                    for (Element tr : trs) {
                        Elements tds = tr.select("td");
                        list.add(new AreaDTO(
                                tds.get(2).text(),
                                tds.get(0).text(),
                                tds.get(0).text(),
                                parentCode,
                                ""));
                    }
                } else {
                    Elements trs = table.select("tr");
                    for (Element tr : trs) {
                        if (tr.attr("class").contains("head")) {
                            continue;
                        }
                        Elements as = tr.select("a");
                        if (as.size() == 0) {
                            Elements tds = tr.select("td");
                            list.add(new AreaDTO(
                                    tds.get(1).text(),
                                    tds.get(0).text(),
                                    tds.get(0).text(),
                                    parentCode,
                                    ""));
                            continue;
                        }
                        urlList.add(baseUrl + as.get(0).attr("href"));
                        list.add(new AreaDTO(
                                as.get(1).text(),
                                as.get(0).text(),
                                getCodeFromUrl(as.get(0).attr("href")),
                                parentCode,
                                baseUrl + as.get(0).attr("href")));
                    }
                }
            }
        }
        DownLoadBean bean = new DownLoadBean(list, urlList, parentCode);
        page.putField("html", new String(page.getHtml().get().getBytes(StandardCharsets.UTF_8)));
        page.putField("bean", bean);

    }

    @Override
    public Site getSite() {
        return site;
    }

    private static String getCodeFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1).replaceAll(".html", "");
    }

    public static void main(String[] args) {
        Spider.create(new MyPageProcessor()).addUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/14/10/141022.html").start();
        System.out.println("倴城镇");
    }
}
