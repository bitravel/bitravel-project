import pandas as pd
import numpy as np

# pandas
# xlrd


def get_metro(address):
    front = address[0:2]
    if address.startswith("경상") or address.startswith('전라') or address.startswith("충청"):
        addition = address[0:1]+address[3:4]
        return addition
    else:
        return front


travel_df = pd.read_excel('../travelspots/2021113_1.xls')
travel_df = travel_df.drop(["우편번호", "관리자", " 유산구분", "개장일", "쉬는날", "체험안내", "체험가능연령", "수용인원", "이용시기", "이용시간"], axis=1)
travel_df["광역지자체"] = travel_df["주소"].str.slice(start=0, stop=3)
travel_df.replace(np.NaN, 'NULL', inplace=True)
print(travel_df['광역지자체'])

#travel = travel_df.to_dict()

#print(travel)
