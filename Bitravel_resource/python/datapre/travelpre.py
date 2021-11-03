import pandas as pd
import numpy as np

# pandas
# xlrd


travel_df = pd.read_excel('../travelspots/2021113_1.xls')
travel_df = travel_df.drop(["우편번호", "관리자", " 유산구분", "개장일", "쉬는날", "체험안내", "체험가능연령", "수용인원", "이용시기", "이용시간"], axis=1)
tmp_list = travel_df["주소"].str.split(' ')
travel_df["광역지자체"] = tmp_list.str[0]
travel_df["기초지자체"] = tmp_list.str[1]
travel_df["광역지자체"] = travel_df["광역지자체"].replace('경상북도', '경북')
travel_df["광역지자체"] = travel_df["광역지자체"].replace('경상남도', '경남')
travel_df["광역지자체"] = travel_df["광역지자체"].replace('전라북도', '전북')
travel_df["광역지자체"] = travel_df["광역지자체"].replace('전라남도', '전남')
travel_df["광역지자체"] = travel_df["광역지자체"].replace('충청북도', '충북')
travel_df["광역지자체"] = travel_df["광역지자체"].replace('충청남도', '충남')
travel_df["광역지자체"] = travel_df["광역지자체"].str.slice(start=0, stop=2)
travel_df.replace(np.NaN, 'NULL', inplace=True)
travel_df["중분류"] = 'NULL'
travel_df["소분류"] = 'NULL'
print(travel_df[["광역지자체", "기초지자체", "소분류"]])

#travel = travel_df.to_dict()

#print(travel)
