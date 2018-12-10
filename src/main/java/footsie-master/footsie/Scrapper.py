import bs4
import requests
from footsie import Share

#'http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=1'

def splitString(s, start, end):
    return (s.split(start))[1].split(end)[0].strip()

def ScrapeWebSite(url):
    response = requests.get(url)
    shares = list()
    if(response.status_code == 200):
        soup = bs4.BeautifulSoup(response.content, "lxml")
        table = soup.findAll(attrs={"summary" : "Companies and Prices"})[0]
        body = table.find('tbody')
        rows = body.findAll('tr')
        for r in rows:
            data = r.findAll('td')
            code = data[0].string
            name = data[1].findChildren()[0].string
            current = data[2].string
            currentPrice = data[3].string
            diff = splitString(str(data[4]), '">', "<")
            per_diff = data[5].string.strip()
            s = Share.Share(code, name, current, currentPrice, diff, per_diff)
            shares.append(s)

    return shares

# ScrapeWebSite('http://www.londonstockexchange.com/exchange/prices-and-markets/stocks/indices/constituents-indices.html?index=UKX&industrySector=&page=1')
