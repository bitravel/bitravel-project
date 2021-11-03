from selenium import webdriver
from selenium.webdriver.common import keys
from selenium.webdriver.common.keys import Keys
# import time import urllib.request
import pandas as pd
import numpy as np

'''
travel_df = pd.read_excel('./유형별 인기 관광지/지역별 관광지 검색 순위_20211103_제주자연.xlsx')
new = travel_df[{'관광지명'}]
print(new)
'''

driver = webdriver.Chrome()
driver.get("https://map.kakao.com/")
elem = driver.find_element_by_name("q")
elem.send_keys('협재해수욕장')
elem.send_keys(Keys.RETURN)
driver.implicitly_wait(time_to_wait=3)
print(driver.find_element_by_xpath('//*[@id="info.search.place.list"]/li[1]/div[5]/div[2]/p[1]').text)


