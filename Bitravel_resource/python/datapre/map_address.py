from selenium.common.exceptions import NoSuchElementException

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import pandas as pd
import numpy as np
import os

#지도에서 검색후 주소 가져오는 코드

path = '../popular_ta'
file_list = os.listdir(path)
print(file_list)

results = []

for file_name_raw in file_list:

    file_name = "./popular_ta/" + file_name_raw
    popular_df = pd.read_excel(file_name)
    new = popular_df['관광지명']
    new1 = popular_df['상세주소']
    for i in range(0, 100):
        driver = webdriver.Chrome() #chromedriver.exe 가 같은 위치에 존재 해야함. (현재 사용하는 크롬과 동일한 버전)
        driver.get("https://map.kakao.com/") #데이터를 가져올 사이트 주소
        elem = driver.find_element_by_name("q")
        elem.send_keys(str(new[i])) #"q" class 위치에 검색어 넣기
        elem.send_keys(Keys.RETURN) #enter 역할
        driver.implicitly_wait(time_to_wait=5) #화면 로딩을 기다려 주기 위한 시간
        try :
            results.append(driver.find_element_by_xpath('//*[@id="info.search.place.list"]/li[1]/div[5]/div[2]/p[1]').text) #가져올 결과 위치(내용)
        except NoSuchElementException :
            results.append(new1[i])
        finally:
            driver.quit()
            print(results)
            new1[i] = results[i] #가져온 주소로 수정
            i = i + 1
    popular_df.to_excel(file_name)
    results.clear()

print(results)