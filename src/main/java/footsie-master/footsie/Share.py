class Share:
    def __init__(self, code, name, current, currentPrice, diff, per_diff):
        self.code = code
        self.name = name
        self.current = current
        self.currentPrice = currentPrice
        self.diff = diff
        self.per_diff = per_diff

    def get_rss_item(self, fe):
        fe.title(self.name)
        fe.id(self.code)
        fe.content("?????" + self.code + ',!' + self.name + ',!' + self.current + ',!' + self.currentPrice + ',!' + self.diff + ',!' + self.per_diff + ',!')
