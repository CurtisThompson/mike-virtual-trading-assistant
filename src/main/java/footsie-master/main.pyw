import tornado.ioloop
import tornado.web
import sys
from feedgen.feed import FeedGenerator
from footsie import Share, Scrapper


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        # self.write("Hello, world")
        #
        # print(page.content)
        p1 = Scrapper.ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=1')
        p2 = Scrapper.ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=2')
        p3 = Scrapper.ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=3')
        p4 = Scrapper.ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=4')
        p5 = Scrapper.ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=5')
        p6 = Scrapper.ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=6')

        shares= p1 + p2 + p3 + p4 + p5 + p6
        fg = FeedGenerator()
        fg.title("src.main.java.LSEScraper Shares")
        fg.link(href='http://localhost:5000',  rel='alternate')
        fg.description('FTSE prices from last 15 mintues.')
        for s in shares:
            fe = fg.add_entry()
            s.get_rss_item(fe)
        self.write(fg.rss_str())
        # print(getattr(shares[0], "name"))
        # self.write("hello")

def make_app():
    return tornado.web.Application([
        (r"/", MainHandler),
    ])


if __name__ == "__main__":
    app = make_app()
    app.listen("4040")
    tornado.ioloop.IOLoop.current().start()
