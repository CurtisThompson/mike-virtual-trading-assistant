
PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;

DROP TABLE stock_history;
DROP TABLE stock_queries;
DROP TABLE stock;
DROP TABLE stock_tag;
DROP TABLE sector;


CREATE TABLE sector(
	SectorId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	SectorName VARCHAR(100) NOT NULL COLLATE NOCASE
);

CREATE TABLE stock(
	StockId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,
	Ticker VARCHAR(10) NOT NULL COLLATE NOCASE,
	StockName VARCHAR(100) NOT NULL COLLATE NOCASE,
	SectorId INTEGER NOT NULL,
	FOREIGN KEY(SectorId) REFERENCES sector(SectorId)
);

CREATE TABLE stock_tag(
	StockTagId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,
	StockId INTEGER,
	Tag VARCHAR(100) NOT NULL UNIQUE COLLATE NOCASE,
	FOREIGN KEY(StockId) REFERENCES stock(StockId)
);

CREATE TABLE stock_history(
	Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	StockId INTEGER NOT NULL,
	Price REAL,
	PercentageChange REAL,
	PriceChange REAL,
	TimeStamp DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
	FOREIGN KEY(StockId) REFERENCES stock(StockId)
);

CREATE TABLE stock_queries(
	StockId INTEGER NOT NULL,
	TimeStamp DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
    QueryId INTEGER PRIMARY KEY AUTOINCREMENT,
	FOREIGN KEY(StockId) REFERENCES stock(StockId)
);

INSERT INTO "sector" VALUES(NULL,"Aerospace and Defense");
INSERT INTO "sector" VALUES(NULL,"Automobiles and Parts");
INSERT INTO "sector" VALUES(NULL,"Banks");
INSERT INTO "sector" VALUES(NULL,"Beverages");
INSERT INTO "sector" VALUES(NULL,"Chemicals");
INSERT INTO "sector" VALUES(NULL,"Construction and Materials");
INSERT INTO "sector" VALUES(NULL,"Electricity");
INSERT INTO "sector" VALUES(NULL,"Electronic and Electrical Equipment");
INSERT INTO "sector" VALUES(NULL,"Equity Investment Instruments");
INSERT INTO "sector" VALUES(NULL,"Financial Services");
INSERT INTO "sector" VALUES(NULL,"Fixed Line Telecommunications");
INSERT INTO "sector" VALUES(NULL,"Food and Drug Retailers");
INSERT INTO "sector" VALUES(NULL,"Food Producers");
INSERT INTO "sector" VALUES(NULL,"Forestry and Paper");
INSERT INTO "sector" VALUES(NULL,"Gas Water and Multiutilities");
INSERT INTO "sector" VALUES(NULL,"General Industrials");
INSERT INTO "sector" VALUES(NULL,"General Retailers");
INSERT INTO "sector" VALUES(NULL,"Health Care Equipment and Services");
INSERT INTO "sector" VALUES(NULL,"Household Goods and Home Construction");
INSERT INTO "sector" VALUES(NULL,"Industrial Metals and Mining");
INSERT INTO "sector" VALUES(NULL,"Life Insurance");
INSERT INTO "sector" VALUES(NULL,"Media");
INSERT INTO "sector" VALUES(NULL,"Mining");
INSERT INTO "sector" VALUES(NULL,"Mobile Telecommunications");
INSERT INTO "sector" VALUES(NULL,"Nonlife Insurance");
INSERT INTO "sector" VALUES(NULL,"Oil and Gas Producers");
INSERT INTO "sector" VALUES(NULL,"Personal Goods");
INSERT INTO "sector" VALUES(NULL,"Pharmaceuticals and Biotechnology");
INSERT INTO "sector" VALUES(NULL,"Real Estate Investment Trusts");
INSERT INTO "sector" VALUES(NULL,"Retail hospitality");
INSERT INTO "sector" VALUES(NULL,"Software and Computer Services");
INSERT INTO "sector" VALUES(NULL,"Support Services");
INSERT INTO "sector" VALUES(NULL,"Tobacco");
INSERT INTO "sector" VALUES(NULL,"Travel and Leisure");


INSERT INTO "stock" VALUES(NULL,"III","3i","10");
INSERT INTO "stock" VALUES(NULL,"ADM","Admiral Group","25");
INSERT INTO "stock" VALUES(NULL,"AAL","Anglo American plc","23");
INSERT INTO "stock" VALUES(NULL,"ANTO","Antofagasta","23");
INSERT INTO "stock" VALUES(NULL,"AHT","Ashtead Group","32");
INSERT INTO "stock" VALUES(NULL,"ABF","Associated British Foods","13");
INSERT INTO "stock" VALUES(NULL,"AZN","AstraZeneca","28");
INSERT INTO "stock" VALUES(NULL,"AV","Aviva","21");
INSERT INTO "stock" VALUES(NULL,"BA","BAE Systems","1");
INSERT INTO "stock" VALUES(NULL,"BARC","Barclays","3");
INSERT INTO "stock" VALUES(NULL,"BDEV","Barratt Developments","19");
INSERT INTO "stock" VALUES(NULL,"BKG","Berkeley Group Holdings","19");
INSERT INTO "stock" VALUES(NULL,"BLT","BHP","23");
INSERT INTO "stock" VALUES(NULL,"BP","BP","26");
INSERT INTO "stock" VALUES(NULL,"BATS","British American Tobacco","33");
INSERT INTO "stock" VALUES(NULL,"BLND","British Land","29");
INSERT INTO "stock" VALUES(NULL,"BTA","BT Group","11");
INSERT INTO "stock" VALUES(NULL,"BNZL","Bunzl","32");
INSERT INTO "stock" VALUES(NULL,"BRBY","Burberry","27");
INSERT INTO "stock" VALUES(NULL,"CCL","Carnival Corporation ","34");
INSERT INTO "stock" VALUES(NULL,"CNA","Centrica","15");
INSERT INTO "stock" VALUES(NULL,"CCH","Coca Cola","4");
INSERT INTO "stock" VALUES(NULL,"CPG","Compass Group","34");
INSERT INTO "stock" VALUES(NULL,"CRH","CRH plc","6");
INSERT INTO "stock" VALUES(NULL,"CRDA","Croda International","5");
INSERT INTO "stock" VALUES(NULL,"DCC","DCC plc","32");
INSERT INTO "stock" VALUES(NULL,"DGE","Diageo","4");
INSERT INTO "stock" VALUES(NULL,"DLG","Direct Line Group","25");
INSERT INTO "stock" VALUES(NULL,"EZJ","easyJet","34");
INSERT INTO "stock" VALUES(NULL,"EVR","Evraz","20");
INSERT INTO "stock" VALUES(NULL,"EXPN","Experian","32");
INSERT INTO "stock" VALUES(NULL,"FERG","Ferguson plc","32");
INSERT INTO "stock" VALUES(NULL,"FRES","Fresnillo plc","23");
INSERT INTO "stock" VALUES(NULL,"GFS","G4S","32");
INSERT INTO "stock" VALUES(NULL,"GKN","GKN","2");
INSERT INTO "stock" VALUES(NULL,"GSK","GlaxoSmithKline","28");
INSERT INTO "stock" VALUES(NULL,"GLEN","Glencore","23");
INSERT INTO "stock" VALUES(NULL,"HLMA","Halma","8");
INSERT INTO "stock" VALUES(NULL,"HMSO","Hammerson","29");
INSERT INTO "stock" VALUES(NULL,"HL","Hargreaves Lansdown","10");
INSERT INTO "stock" VALUES(NULL,"HSBA","HSBC","3");
INSERT INTO "stock" VALUES(NULL,"IMB","Imperial Brands","33");
INSERT INTO "stock" VALUES(NULL,"INF","Informa","22");
INSERT INTO "stock" VALUES(NULL,"IHG","InterContinental Hotels Group","34");
INSERT INTO "stock" VALUES(NULL,"IAG","International Airlines Group","34");
INSERT INTO "stock" VALUES(NULL,"ITRK","Intertek","32");
INSERT INTO "stock" VALUES(NULL,"ITV","ITV plc","22");
INSERT INTO "stock" VALUES(NULL,"JMAT","Johnson Matthey","5");
INSERT INTO "stock" VALUES(NULL,"JE","Just Eat","17");
INSERT INTO "stock" VALUES(NULL,"KGF","Kingfisher plc","17");
INSERT INTO "stock" VALUES(NULL,"LAND","Land Securities","29");
INSERT INTO "stock" VALUES(NULL,"LGEN","Legal & General","21");
INSERT INTO "stock" VALUES(NULL,"LLOY","Lloyds Banking Group","3");
INSERT INTO "stock" VALUES(NULL,"LSE","London Stock Exchange Group","10");
INSERT INTO "stock" VALUES(NULL,"MKS","Marks & Spencer","17");
INSERT INTO "stock" VALUES(NULL,"MDC","Mediclinic International","18");
INSERT INTO "stock" VALUES(NULL,"MCRO","Micro Focus","31");
INSERT INTO "stock" VALUES(NULL,"MNDI","Mondi","14");
INSERT INTO "stock" VALUES(NULL,"MRW","Morrisons","12");
INSERT INTO "stock" VALUES(NULL,"NG","National Grid plc","15");
INSERT INTO "stock" VALUES(NULL,"NXT","Next plc","17");
INSERT INTO "stock" VALUES(NULL,"NMC","NMC Health","18");
INSERT INTO "stock" VALUES(NULL,"OML","Old Mutual","21");
INSERT INTO "stock" VALUES(NULL,"PPB","Paddy Power Betfair","34");
INSERT INTO "stock" VALUES(NULL,"PSON","Pearson PLC","22");
INSERT INTO "stock" VALUES(NULL,"PSN","Persimmon plc","19");
INSERT INTO "stock" VALUES(NULL,"PRU","Prudential plc","21");
INSERT INTO "stock" VALUES(NULL,"RRS","Randgold Resources","23");
INSERT INTO "stock" VALUES(NULL,"RB","Reckitt Benckiser","19");
INSERT INTO "stock" VALUES(NULL,"REL","RELX Group","22");
INSERT INTO "stock" VALUES(NULL,"RTO","Rentokil Initial","32");
INSERT INTO "stock" VALUES(NULL,"RIO","Rio Tinto Group","23");
INSERT INTO "stock" VALUES(NULL,"RR","Rolls-Royce Holdings","1");
INSERT INTO "stock" VALUES(NULL,"RBS","The Royal Bank of Scotland Group","3");
INSERT INTO "stock" VALUES(NULL,"RDSA","Royal Dutch Shell","26");
INSERT INTO "stock" VALUES(NULL,"RSA","RSA Insurance Group","25");
INSERT INTO "stock" VALUES(NULL,"SGE","Sage Group","31");
INSERT INTO "stock" VALUES(NULL,"SBRY","Sainsbury's","12");
INSERT INTO "stock" VALUES(NULL,"SDR","Schroders","10");
INSERT INTO "stock" VALUES(NULL,"SMT","Scottish Mortgage Investment Trust","9");
INSERT INTO "stock" VALUES(NULL,"SGRO","Segro","29");
INSERT INTO "stock" VALUES(NULL,"SVT","Severn Trent","15");
INSERT INTO "stock" VALUES(NULL,"SHP","Shire plc","28");
INSERT INTO "stock" VALUES(NULL,"SKY","Sky plc","22");
INSERT INTO "stock" VALUES(NULL,"SN","Smith & Nephew","18");
INSERT INTO "stock" VALUES(NULL,"SMDS","Smith, D.S.","16");
INSERT INTO "stock" VALUES(NULL,"SMIN","Smiths Group","16");
INSERT INTO "stock" VALUES(NULL,"SKG","Smurfit Kappa","16");
INSERT INTO "stock" VALUES(NULL,"SSE","SSE plc","7");
INSERT INTO "stock" VALUES(NULL,"STAN","Standard Chartered","3");
INSERT INTO "stock" VALUES(NULL,"SLA","Standard Life Aberdeen","10");
INSERT INTO "stock" VALUES(NULL,"STJ","St. James's Place plc","21");
INSERT INTO "stock" VALUES(NULL,"TW","Taylor Wimpey","19");
INSERT INTO "stock" VALUES(NULL,"TSCO","Tesco","12");
INSERT INTO "stock" VALUES(NULL,"TUI","TUI Group","34");
INSERT INTO "stock" VALUES(NULL,"ULVR","Unilever","27");
INSERT INTO "stock" VALUES(NULL,"UU","United Utilities","15");
INSERT INTO "stock" VALUES(NULL,"VOD","Vodafone Group","24");
INSERT INTO "stock" VALUES(NULL,"WTB","Whitbread","30");
INSERT INTO "stock" VALUES(NULL,"WPP","WPP plc","22");


INSERT INTO "stock_tag" VALUES(NULL,"1","3i");
INSERT INTO "stock_tag" VALUES(NULL,"1","iii");
INSERT INTO "stock_tag" VALUES(NULL,"1","3i grp.");
INSERT INTO "stock_tag" VALUES(NULL,"2","admiral group");
INSERT INTO "stock_tag" VALUES(NULL,"2","adm");
INSERT INTO "stock_tag" VALUES(NULL,"2","admiral");
INSERT INTO "stock_tag" VALUES(NULL,"2","admiral grp");
INSERT INTO "stock_tag" VALUES(NULL,"3","anglo american");
INSERT INTO "stock_tag" VALUES(NULL,"3","aal");
INSERT INTO "stock_tag" VALUES(NULL,"3","anglo american plc");
INSERT INTO "stock_tag" VALUES(NULL,"4","antofagasta");
INSERT INTO "stock_tag" VALUES(NULL,"4","anto");
INSERT INTO "stock_tag" VALUES(NULL,"5","aht");
INSERT INTO "stock_tag" VALUES(NULL,"5","ashtead grp.");
INSERT INTO "stock_tag" VALUES(NULL,"5","ashtead");
INSERT INTO "stock_tag" VALUES(NULL,"5","ashtead group");
INSERT INTO "stock_tag" VALUES(NULL,"6","a.b.food");
INSERT INTO "stock_tag" VALUES(NULL,"6","abf");
INSERT INTO "stock_tag" VALUES(NULL,"6","associated british foods");
INSERT INTO "stock_tag" VALUES(NULL,"7","astrazeneca");
INSERT INTO "stock_tag" VALUES(NULL,"7","azn");
INSERT INTO "stock_tag" VALUES(NULL,"8","av");
INSERT INTO "stock_tag" VALUES(NULL,"8","aviva");
INSERT INTO "stock_tag" VALUES(NULL,"9","bae sys.");
INSERT INTO "stock_tag" VALUES(NULL,"9","bae systems");
INSERT INTO "stock_tag" VALUES(NULL,"9","bae");
INSERT INTO "stock_tag" VALUES(NULL,"9","ba");
INSERT INTO "stock_tag" VALUES(NULL,"10","barclays");
INSERT INTO "stock_tag" VALUES(NULL,"10","barc");
INSERT INTO "stock_tag" VALUES(NULL,"11","barratt developments");
INSERT INTO "stock_tag" VALUES(NULL,"11","bdev");
INSERT INTO "stock_tag" VALUES(NULL,"11","barratt devel.");
INSERT INTO "stock_tag" VALUES(NULL,"12","berkeley");
INSERT INTO "stock_tag" VALUES(NULL,"12","berkeley group");
INSERT INTO "stock_tag" VALUES(NULL,"12","berkeley group holdings");
INSERT INTO "stock_tag" VALUES(NULL,"12","berkeley gp.hld");
INSERT INTO "stock_tag" VALUES(NULL,"12","bkg");
INSERT INTO "stock_tag" VALUES(NULL,"13","blt");
INSERT INTO "stock_tag" VALUES(NULL,"13","bhp billiton");
INSERT INTO "stock_tag" VALUES(NULL,"13","bhp");
INSERT INTO "stock_tag" VALUES(NULL,"14","bp");
INSERT INTO "stock_tag" VALUES(NULL,"15","br.amer.tob.");
INSERT INTO "stock_tag" VALUES(NULL,"15","bats");
INSERT INTO "stock_tag" VALUES(NULL,"15","british american tobacco");
INSERT INTO "stock_tag" VALUES(NULL,"16","blnd");
INSERT INTO "stock_tag" VALUES(NULL,"16","br.land");
INSERT INTO "stock_tag" VALUES(NULL,"16","british land");
INSERT INTO "stock_tag" VALUES(NULL,"17","bta");
INSERT INTO "stock_tag" VALUES(NULL,"17","bt");
INSERT INTO "stock_tag" VALUES(NULL,"17","bt group");
INSERT INTO "stock_tag" VALUES(NULL,"18","bnzl");
INSERT INTO "stock_tag" VALUES(NULL,"18","bunzl");
INSERT INTO "stock_tag" VALUES(NULL,"19","brby");
INSERT INTO "stock_tag" VALUES(NULL,"19","burberry grp");
INSERT INTO "stock_tag" VALUES(NULL,"19","burberry");
INSERT INTO "stock_tag" VALUES(NULL,"20","carnival");
INSERT INTO "stock_tag" VALUES(NULL,"20","ccl");
INSERT INTO "stock_tag" VALUES(NULL,"20","carnival corporation");
INSERT INTO "stock_tag" VALUES(NULL,"20","carnival corporation ");
INSERT INTO "stock_tag" VALUES(NULL,"21","cna");
INSERT INTO "stock_tag" VALUES(NULL,"21","centrica");
INSERT INTO "stock_tag" VALUES(NULL,"22","cocacola hbc ag");
INSERT INTO "stock_tag" VALUES(NULL,"22","cch");
INSERT INTO "stock_tag" VALUES(NULL,"22","coca cola");
INSERT INTO "stock_tag" VALUES(NULL,"23","compass group");
INSERT INTO "stock_tag" VALUES(NULL,"23","cpg");
INSERT INTO "stock_tag" VALUES(NULL,"23","compass");
INSERT INTO "stock_tag" VALUES(NULL,"24","crh");
INSERT INTO "stock_tag" VALUES(NULL,"24","crh plc");
INSERT INTO "stock_tag" VALUES(NULL,"25","croda international");
INSERT INTO "stock_tag" VALUES(NULL,"25","croda");
INSERT INTO "stock_tag" VALUES(NULL,"25","crda");
INSERT INTO "stock_tag" VALUES(NULL,"25","croda intl.");
INSERT INTO "stock_tag" VALUES(NULL,"26","dcc");
INSERT INTO "stock_tag" VALUES(NULL,"26","dcc plc");
INSERT INTO "stock_tag" VALUES(NULL,"27","dge");
INSERT INTO "stock_tag" VALUES(NULL,"27","diageo");
INSERT INTO "stock_tag" VALUES(NULL,"28","dlg");
INSERT INTO "stock_tag" VALUES(NULL,"28","direct line");
INSERT INTO "stock_tag" VALUES(NULL,"28","direct line group");
INSERT INTO "stock_tag" VALUES(NULL,"29","ezj");
INSERT INTO "stock_tag" VALUES(NULL,"29","easyjet");
INSERT INTO "stock_tag" VALUES(NULL,"29","easy jet");
INSERT INTO "stock_tag" VALUES(NULL,"30","evraz");
INSERT INTO "stock_tag" VALUES(NULL,"30","evr");
INSERT INTO "stock_tag" VALUES(NULL,"31","experian");
INSERT INTO "stock_tag" VALUES(NULL,"31","expn");
INSERT INTO "stock_tag" VALUES(NULL,"32","ferg");
INSERT INTO "stock_tag" VALUES(NULL,"32","ferguson");
INSERT INTO "stock_tag" VALUES(NULL,"32","ferguson plc");
INSERT INTO "stock_tag" VALUES(NULL,"33","fres");
INSERT INTO "stock_tag" VALUES(NULL,"33","fresnillo plc");
INSERT INTO "stock_tag" VALUES(NULL,"33","fresnillo");
INSERT INTO "stock_tag" VALUES(NULL,"34","gfs");
INSERT INTO "stock_tag" VALUES(NULL,"34","g4s");
INSERT INTO "stock_tag" VALUES(NULL,"35","gkn");
INSERT INTO "stock_tag" VALUES(NULL,"36","gsk");
INSERT INTO "stock_tag" VALUES(NULL,"36","glaxosmithkline");
INSERT INTO "stock_tag" VALUES(NULL,"37","glen");
INSERT INTO "stock_tag" VALUES(NULL,"37","glencore");
INSERT INTO "stock_tag" VALUES(NULL,"38","halma");
INSERT INTO "stock_tag" VALUES(NULL,"38","hlma");
INSERT INTO "stock_tag" VALUES(NULL,"39","hmso");
INSERT INTO "stock_tag" VALUES(NULL,"39","hammerson");
INSERT INTO "stock_tag" VALUES(NULL,"40","hargreaves lans");
INSERT INTO "stock_tag" VALUES(NULL,"40","hargreaves lansdown");
INSERT INTO "stock_tag" VALUES(NULL,"40","hl");
INSERT INTO "stock_tag" VALUES(NULL,"41","hsbc hldgs.uk");
INSERT INTO "stock_tag" VALUES(NULL,"41","hsbc");
INSERT INTO "stock_tag" VALUES(NULL,"41","hsba");
INSERT INTO "stock_tag" VALUES(NULL,"42","imp.brands");
INSERT INTO "stock_tag" VALUES(NULL,"42","imperial brands");
INSERT INTO "stock_tag" VALUES(NULL,"42","imb");
INSERT INTO "stock_tag" VALUES(NULL,"43","inf");
INSERT INTO "stock_tag" VALUES(NULL,"43","informa");
INSERT INTO "stock_tag" VALUES(NULL,"44","ihg");
INSERT INTO "stock_tag" VALUES(NULL,"44","intercontinental hotels group");
INSERT INTO "stock_tag" VALUES(NULL,"44","intercontinental hotels");
INSERT INTO "stock_tag" VALUES(NULL,"44","intercon. hotel");
INSERT INTO "stock_tag" VALUES(NULL,"45","international airlines group");
INSERT INTO "stock_tag" VALUES(NULL,"45","international airlines");
INSERT INTO "stock_tag" VALUES(NULL,"45","iag");
INSERT INTO "stock_tag" VALUES(NULL,"45","intertek group");
INSERT INTO "stock_tag" VALUES(NULL,"46","intl consol air");
INSERT INTO "stock_tag" VALUES(NULL,"46","itrk");
INSERT INTO "stock_tag" VALUES(NULL,"46","intertek");
INSERT INTO "stock_tag" VALUES(NULL,"47","itv plc");
INSERT INTO "stock_tag" VALUES(NULL,"47","itv");
INSERT INTO "stock_tag" VALUES(NULL,"48","johnson matthey");
INSERT INTO "stock_tag" VALUES(NULL,"48","jmat");
INSERT INTO "stock_tag" VALUES(NULL,"49","je");
INSERT INTO "stock_tag" VALUES(NULL,"49","just eat");
INSERT INTO "stock_tag" VALUES(NULL,"50","kingfisher plc");
INSERT INTO "stock_tag" VALUES(NULL,"50","kgf");
INSERT INTO "stock_tag" VALUES(NULL,"50","kingfisher");
INSERT INTO "stock_tag" VALUES(NULL,"51","land");
INSERT INTO "stock_tag" VALUES(NULL,"51","land securities");
INSERT INTO "stock_tag" VALUES(NULL,"51","land secs.");
INSERT INTO "stock_tag" VALUES(NULL,"52","legal & general");
INSERT INTO "stock_tag" VALUES(NULL,"52","legal&gen.");
INSERT INTO "stock_tag" VALUES(NULL,"52","legal and general");
INSERT INTO "stock_tag" VALUES(NULL,"52","lgen");
INSERT INTO "stock_tag" VALUES(NULL,"53","lloyds grp.");
INSERT INTO "stock_tag" VALUES(NULL,"53","lloyds banking group");
INSERT INTO "stock_tag" VALUES(NULL,"53","lloy");
INSERT INTO "stock_tag" VALUES(NULL,"54","lse");
INSERT INTO "stock_tag" VALUES(NULL,"54","london stock exchange group");
INSERT INTO "stock_tag" VALUES(NULL,"54","lon.stk.exch");
INSERT INTO "stock_tag" VALUES(NULL,"55","marks & spencer");
INSERT INTO "stock_tag" VALUES(NULL,"55","marks and spencer");
INSERT INTO "stock_tag" VALUES(NULL,"55","marks & sp.");
INSERT INTO "stock_tag" VALUES(NULL,"55","mks");
INSERT INTO "stock_tag" VALUES(NULL,"56","mediclinic");
INSERT INTO "stock_tag" VALUES(NULL,"56","mediclinic international");
INSERT INTO "stock_tag" VALUES(NULL,"56","mdc");
INSERT INTO "stock_tag" VALUES(NULL,"57","micro focus");
INSERT INTO "stock_tag" VALUES(NULL,"57","mcro");
INSERT INTO "stock_tag" VALUES(NULL,"58","mondi");
INSERT INTO "stock_tag" VALUES(NULL,"58","mndi");
INSERT INTO "stock_tag" VALUES(NULL,"59","morrison (wm)");
INSERT INTO "stock_tag" VALUES(NULL,"59","morrisons");
INSERT INTO "stock_tag" VALUES(NULL,"59","mrw");
INSERT INTO "stock_tag" VALUES(NULL,"60","national grid");
INSERT INTO "stock_tag" VALUES(NULL,"60","ng");
INSERT INTO "stock_tag" VALUES(NULL,"60","national grid plc");
INSERT INTO "stock_tag" VALUES(NULL,"61","next plc");
INSERT INTO "stock_tag" VALUES(NULL,"61","nxt");
INSERT INTO "stock_tag" VALUES(NULL,"61","next");
INSERT INTO "stock_tag" VALUES(NULL,"62","nmc health");
INSERT INTO "stock_tag" VALUES(NULL,"62","nmc");
INSERT INTO "stock_tag" VALUES(NULL,"63","old mutual");
INSERT INTO "stock_tag" VALUES(NULL,"63","oml");
INSERT INTO "stock_tag" VALUES(NULL,"64","paddy power betfair");
INSERT INTO "stock_tag" VALUES(NULL,"64","betfair");
INSERT INTO "stock_tag" VALUES(NULL,"64","paddy pwr bet");
INSERT INTO "stock_tag" VALUES(NULL,"64","paddy power");
INSERT INTO "stock_tag" VALUES(NULL,"64","ppb");
INSERT INTO "stock_tag" VALUES(NULL,"65","pson");
INSERT INTO "stock_tag" VALUES(NULL,"65","pearson plc");
INSERT INTO "stock_tag" VALUES(NULL,"65","pearson");
INSERT INTO "stock_tag" VALUES(NULL,"66","persimmon");
INSERT INTO "stock_tag" VALUES(NULL,"66","persimmon plc");
INSERT INTO "stock_tag" VALUES(NULL,"66","psn");
INSERT INTO "stock_tag" VALUES(NULL,"67","prudential");
INSERT INTO "stock_tag" VALUES(NULL,"67","pru");
INSERT INTO "stock_tag" VALUES(NULL,"67","prudential plc");
INSERT INTO "stock_tag" VALUES(NULL,"68","randgold res.");
INSERT INTO "stock_tag" VALUES(NULL,"68","randgold resources");
INSERT INTO "stock_tag" VALUES(NULL,"68","rrs");
INSERT INTO "stock_tag" VALUES(NULL,"69","reckitt benckiser");
INSERT INTO "stock_tag" VALUES(NULL,"69","rb");
INSERT INTO "stock_tag" VALUES(NULL,"69","reckitt ben. gp");
INSERT INTO "stock_tag" VALUES(NULL,"70","rel");
INSERT INTO "stock_tag" VALUES(NULL,"70","relx group");
INSERT INTO "stock_tag" VALUES(NULL,"70","relx");
INSERT INTO "stock_tag" VALUES(NULL,"71","rto");
INSERT INTO "stock_tag" VALUES(NULL,"71","rentokil initial");
INSERT INTO "stock_tag" VALUES(NULL,"71","rentokil");
INSERT INTO "stock_tag" VALUES(NULL,"71","rentokil initl.");
INSERT INTO "stock_tag" VALUES(NULL,"72","rio");
INSERT INTO "stock_tag" VALUES(NULL,"72","rio tinto group");
INSERT INTO "stock_tag" VALUES(NULL,"72","rio tinto");
INSERT INTO "stock_tag" VALUES(NULL,"73","rolls-royce hlg");
INSERT INTO "stock_tag" VALUES(NULL,"73","rolls-royce holdings");
INSERT INTO "stock_tag" VALUES(NULL,"73","rr");
INSERT INTO "stock_tag" VALUES(NULL,"73","rolls royce");
INSERT INTO "stock_tag" VALUES(NULL,"74","royal bank scot");
INSERT INTO "stock_tag" VALUES(NULL,"74","the royal bank of scotland");
INSERT INTO "stock_tag" VALUES(NULL,"74","the royal bank of scotland group");
INSERT INTO "stock_tag" VALUES(NULL,"74","rbs");
INSERT INTO "stock_tag" VALUES(NULL,"75","royal dutch shell");
INSERT INTO "stock_tag" VALUES(NULL,"75","rdsa");
INSERT INTO "stock_tag" VALUES(NULL,"75","rds 'a'");
INSERT INTO "stock_tag" VALUES(NULL,"76","rsa");
INSERT INTO "stock_tag" VALUES(NULL,"76","rsa insurance group");
INSERT INTO "stock_tag" VALUES(NULL,"76","rsa ins.");
INSERT INTO "stock_tag" VALUES(NULL,"76","rsa insurance");
INSERT INTO "stock_tag" VALUES(NULL,"77","sage group");
INSERT INTO "stock_tag" VALUES(NULL,"77","sge");
INSERT INTO "stock_tag" VALUES(NULL,"77","sage grp.");
INSERT INTO "stock_tag" VALUES(NULL,"78","sainsbury's");
INSERT INTO "stock_tag" VALUES(NULL,"78","sainsbury");
INSERT INTO "stock_tag" VALUES(NULL,"78","sbry");
INSERT INTO "stock_tag" VALUES(NULL,"78","sainsbury(j)");
INSERT INTO "stock_tag" VALUES(NULL,"79","sdr");
INSERT INTO "stock_tag" VALUES(NULL,"79","schroders");
INSERT INTO "stock_tag" VALUES(NULL,"80","scottish mortgage investment trust");
INSERT INTO "stock_tag" VALUES(NULL,"80","scottish mort");
INSERT INTO "stock_tag" VALUES(NULL,"80","smt");
INSERT INTO "stock_tag" VALUES(NULL,"81","segro");
INSERT INTO "stock_tag" VALUES(NULL,"81","sgro");
INSERT INTO "stock_tag" VALUES(NULL,"82","svt");
INSERT INTO "stock_tag" VALUES(NULL,"82","severn trent");
INSERT INTO "stock_tag" VALUES(NULL,"83","shp");
INSERT INTO "stock_tag" VALUES(NULL,"83","shire");
INSERT INTO "stock_tag" VALUES(NULL,"83","shire plc");
INSERT INTO "stock_tag" VALUES(NULL,"84","sky plc");
INSERT INTO "stock_tag" VALUES(NULL,"84","sky");
INSERT INTO "stock_tag" VALUES(NULL,"85","smith&nephew");
INSERT INTO "stock_tag" VALUES(NULL,"85","smith and nephew");
INSERT INTO "stock_tag" VALUES(NULL,"85","sn");
INSERT INTO "stock_tag" VALUES(NULL,"85","smith & nephew");
INSERT INTO "stock_tag" VALUES(NULL,"86","smith");
INSERT INTO "stock_tag" VALUES(NULL,"86","smith d s");
INSERT INTO "stock_tag" VALUES(NULL,"86"," d.s.");
INSERT INTO "stock_tag" VALUES(NULL,"86","smds");
INSERT INTO "stock_tag" VALUES(NULL,"86","smith(ds)");
INSERT INTO "stock_tag" VALUES(NULL,"87","smin");
INSERT INTO "stock_tag" VALUES(NULL,"87","smiths group");
INSERT INTO "stock_tag" VALUES(NULL,"88","smurfit kappa");
INSERT INTO "stock_tag" VALUES(NULL,"88","smurfit kap.");
INSERT INTO "stock_tag" VALUES(NULL,"88","skg");
INSERT INTO "stock_tag" VALUES(NULL,"89","sse");
INSERT INTO "stock_tag" VALUES(NULL,"89","sse plc");
INSERT INTO "stock_tag" VALUES(NULL,"90","standard chartered");
INSERT INTO "stock_tag" VALUES(NULL,"90","st.james's plac");
INSERT INTO "stock_tag" VALUES(NULL,"90","stan");
INSERT INTO "stock_tag" VALUES(NULL,"91","sla");
INSERT INTO "stock_tag" VALUES(NULL,"91","stand.chart.");
INSERT INTO "stock_tag" VALUES(NULL,"91","standard life aberdeen");
INSERT INTO "stock_tag" VALUES(NULL,"92","stj");
INSERT INTO "stock_tag" VALUES(NULL,"92","st. james's place plc");
INSERT INTO "stock_tag" VALUES(NULL,"92","st. james's place");
INSERT INTO "stock_tag" VALUES(NULL,"92","std life aber");
INSERT INTO "stock_tag" VALUES(NULL,"93","tw");
INSERT INTO "stock_tag" VALUES(NULL,"93","taylor wimpey");
INSERT INTO "stock_tag" VALUES(NULL,"94","tsco");
INSERT INTO "stock_tag" VALUES(NULL,"94","tesco");
INSERT INTO "stock_tag" VALUES(NULL,"95","tui");
INSERT INTO "stock_tag" VALUES(NULL,"95","tui group");
INSERT INTO "stock_tag" VALUES(NULL,"95","tui ag");
INSERT INTO "stock_tag" VALUES(NULL,"96","ulvr");
INSERT INTO "stock_tag" VALUES(NULL,"96","unilever");
INSERT INTO "stock_tag" VALUES(NULL,"97","utd. utilities");
INSERT INTO "stock_tag" VALUES(NULL,"97","uu");
INSERT INTO "stock_tag" VALUES(NULL,"97","united utilities");
INSERT INTO "stock_tag" VALUES(NULL,"98","vodafone grp.");
INSERT INTO "stock_tag" VALUES(NULL,"98","vodafone group");
INSERT INTO "stock_tag" VALUES(NULL,"98","vod");
INSERT INTO "stock_tag" VALUES(NULL,"98","vodafone");
INSERT INTO "stock_tag" VALUES(NULL,"99","wtb");
INSERT INTO "stock_tag" VALUES(NULL,"99","whitbread");
INSERT INTO "stock_tag" VALUES(NULL,"100","wpp plc");
INSERT INTO "stock_tag" VALUES(NULL,"100","wpp");
INSERT INTO "stock_tag" VALUES(NULL,"86","Smith, D.S.");

/*
INSERT INTO "stock_history" VALUES(1,2,3.0,5.1,3.0,'2018-02-20 00:00:01');
INSERT INTO "stock_history" VALUES(2,3,3.0,2.0,3.0,'2018-02-21 00:00:01');
INSERT INTO "stock_history" VALUES(3,4,3.0,3.0,3.0,'2018-02-22 00:00:01');
INSERT INTO "stock_history" VALUES(4,2,3.0,7.0,3.0,'2018-02-23 00:00:01');
INSERT INTO "stock_history" VALUES(5,6,1.0,1.0,3.0,'2018-02-24 00:00:01');
INSERT INTO "stock_history" VALUES(6,7,6.0,6.0,3.0,'2018-02-25 00:00:01');


INSERT INTO "stock_queries" VALUES(1,'2018-02-20 00:00:01');
INSERT INTO "stock_queries" VALUES(1,'2018-02-21 00:00:01');
INSERT INTO "stock_queries" VALUES(3,'2018-02-21 00:00:02');
INSERT INTO "stock_queries" VALUES(3,'2018-02-21 00:00:03');
INSERT INTO "stock_queries" VALUES(3,'2018-02-22 00:00:04');
INSERT INTO "stock_queries" VALUES(2,'2018-02-20 00:00:02');
INSERT INTO "stock_queries" VALUES(2,'2018-02-20 00:00:03');
INSERT INTO "stock_queries" VALUES(3,'2018-02-21 00:00:04');
INSERT INTO "stock_queries" VALUES(3,'2018-02-21 00:00:05');
INSERT INTO "stock_queries" VALUES(3,'2018-02-21 00:00:06');
INSERT INTO "stock_queries" VALUES(3,'2018-02-22 00:00:07');
INSERT INTO "stock_queries" VALUES(4,'2018-02-20 00:00:08');
INSERT INTO "stock_queries" VALUES(5,'2018-02-20 00:00:09');
*/

COMMIT;