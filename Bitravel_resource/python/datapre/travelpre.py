import pandas as pd
import numpy as np
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.keys import Keys


# pandas
# xlrd


def get_address(row_name):
    try:
        driver = webdriver.Chrome()
        driver.get("https://map.kakao.com/")
        elem = driver.find_element_by_name("q")
        elem.send_keys(row_name)
        elem.send_keys(Keys.RETURN)
        driver.implicitly_wait(time_to_wait=5)
        result = driver.find_element_by_xpath('//*[@id="info.search.place.list"]/li[1]/div[5]/div[2]/p[1]').text
        driver.quit()
    except NoSuchElementException:
        result = ''
    return result


for i in range(28, 29):
    filename = '../travelspots/2021113_'
    if i == 20 or i == 44 or i == 27 or i == 28:
        filename = filename + str(i) + '.xlsx'
    else:
        filename = filename + str(i) + '.xls'
    print(filename)
    travel_df = pd.read_excel(filename)
    travel_df = travel_df.drop(["우편번호", "관리자", " 유산구분", "개장일", "쉬는날", "체험안내", "체험가능연령", "수용인원", "이용시기", "이용시간"], axis=1)

    tmp_list = travel_df["주소"].values.tolist()
    j = 0
    for row in tmp_list:
        if len(row) < 6:
            name = travel_df.loc[j, '명칭']
            travel_df.loc[j, '주소'] = get_address(name)
        for char in row:
            if char.encode().isalpha():
                name = travel_df.loc[j, '명칭']
                travel_df.loc[j, '주소'] = get_address(name)
                break
        j = j + 1
    tmp_list = travel_df["주소"].str.split(' ')

    travel_df["광역지자체"] = tmp_list.str[0]
    travel_df["기초지자체"] = tmp_list.str[1]
    j = -1
    for row in tmp_list.str[1]:
        j = j + 1
        if row is not str:
            continue
        print(travel_df.loc[j, '명칭'], j)
        if travel_df.loc[j, '명칭'].contains("특구"):
            travel_df.loc[j, '기초지자체'] = ''
            print(j, travel_df.loc[j, '기초지자체'])
            continue
        if len(row) > 4:
            if row[3] == '시' or row[3] == '군' or row[3] == '구':
                travel_df.loc[j, '기초지자체'] = row[0:4]
                continue
            if row[2] == '시' or row[2] == '군' or row[2] == '구':
                travel_df.loc[j, '기초지자체'] = row[0:3]
                continue
            if row[1] == '시' or row[1] == '군' or row[1] == '구':
                travel_df.loc[j, '기초지자체'] = row[0:2]

#    index_special = travel_df[travel_df['명칭'].str.contains("특구")].index
#    travel_df = travel_df.drop(index_special)

    travel_df["광역지자체"] = travel_df["광역지자체"].replace('경상북도', '경북')
    travel_df["광역지자체"] = travel_df["광역지자체"].replace('경상남도', '경남')
    travel_df["광역지자체"] = travel_df["광역지자체"].replace('전라북도', '전북')
    travel_df["광역지자체"] = travel_df["광역지자체"].replace('전라남도', '전남')
    travel_df["광역지자체"] = travel_df["광역지자체"].replace('충청북도', '충북')
    travel_df["광역지자체"] = travel_df["광역지자체"].replace('충청남도', '충남')
    travel_df["광역지자체"] = travel_df["광역지자체"].str.slice(start=0, stop=2)
    travel_df["기초지자체"] = travel_df["기초지자체"].str.replace(',', '')
    travel_df.replace(np.NaN, '', inplace=True)
    travel_df["중분류"] = ''
    travel_df["소분류"] = ''

    travel_df["주소"] = travel_df["주소"].str.replace('\n', '')
    travel_df["주소"] = travel_df["주소"].str.replace('\t', '')

    travel_df["개요"] = travel_df["개요"].str.replace('\t', '')
    travel_df["문의 및 안내"] = travel_df["문의 및 안내"].str.replace('\t', '')
    travel_df["상세정보"] = travel_df["상세정보"].str.replace('\t', '')

    travel_df["개요"] = travel_df["개요"].str.replace('<br />\n', '<br>')
    travel_df["개요"] = travel_df["개요"].str.replace('<br>\n', '<br>')
    travel_df["개요"] = travel_df["개요"].str.replace('\n', '<br>')
    travel_df["상세정보"] = travel_df["상세정보"].str.replace('<br />\n', '<br>')
    travel_df["상세정보"] = travel_df["상세정보"].str.replace('<br>\n', '<br>')
    travel_df["상세정보"] = travel_df["상세정보"].str.replace('\n', '<br>')
    travel_df["문의 및 안내"] = travel_df["문의 및 안내"].str.replace('<br />\n', '<br>')
    travel_df["문의 및 안내"] = travel_df["문의 및 안내"].str.replace('<br>\n', '<br>')
    travel_df["문의 및 안내"] = travel_df["문의 및 안내"].str.replace('\n', '<br>')

    for index, row in travel_df.iterrows():
        # print(row)
        if row['전화번호'] != '':
            if row['전화번호'] != row['문의 및 안내']:
                travel_df.loc[index, '문의 및 안내'] = row['전화번호'] + "<br>" + row['문의 및 안내']
        if row['광역지자체'] == '세종':
            travel_df.loc[index, '기초지자체'] = '세종시'
    travel_df = travel_df.drop(['전화번호'], axis=1)

    if i == 1:
        with pd.ExcelWriter('../preprocessing/outputtravel.xlsx', mode='w') as writer:
            travel_df.to_excel(writer)
    else:
        with pd.ExcelWriter('../preprocessing/outputtravel.xlsx', mode='a', if_sheet_exists='replace') as writer:
            sheet_name = 'page'+str(i)
            travel_df.to_excel(writer, sheet_name=sheet_name)
    print(i, 'is done.')
