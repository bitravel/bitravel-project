import pandas as pd
import numpy as np

# pandas
# xlrd


culture_df = pd.read_excel('../travelspots/문화시설1.xls')
culture_df = culture_df.drop(["우편번호", "관리자", "규모", "수용인원", "이용시간", "쉬는날", "이용요금", "할인정보", "관람소요시간", "주차요금"], axis=1)
tmp_list = culture_df["주소"].str.split(' ')
culture_df["광역지자체"] = tmp_list.str[0]
culture_df["기초지자체"] = tmp_list.str[1]
culture_df["광역지자체"] = culture_df["광역지자체"].replace('경상북도', '경북')
culture_df["광역지자체"] = culture_df["광역지자체"].replace('경상남도', '경남')
culture_df["광역지자체"] = culture_df["광역지자체"].replace('전라북도', '전북')
culture_df["광역지자체"] = culture_df["광역지자체"].replace('전라남도', '전남')
culture_df["광역지자체"] = culture_df["광역지자체"].replace('충청북도', '충북')
culture_df["광역지자체"] = culture_df["광역지자체"].replace('충청남도', '충남')
culture_df["광역지자체"] = culture_df["광역지자체"].str.slice(start=0, stop=2)
culture_df.replace(np.NaN, '', inplace=True)
culture_df["중분류"] = ''
culture_df["소분류"] = ''
culture_df["주소"] = culture_df["주소"].str.replace('\n', '')
culture_df["주소"] = culture_df["주소"].str.replace('\t', '')

culture_df["개요"] = culture_df["개요"].str.replace('\t', '')
culture_df["문의 및 안내"] = culture_df["문의 및 안내"].str.replace('\t', '')
culture_df["상세정보"] = culture_df["상세정보"].str.replace('\t', '')

culture_df["개요"] = culture_df["개요"].str.replace('<br />\n', '<br>')
culture_df["개요"] = culture_df["개요"].str.replace('<br>\n', '<br>')
culture_df["개요"] = culture_df["개요"].str.replace('\n', '<br>')
culture_df["상세정보"] = culture_df["상세정보"].str.replace('<br />\n', '<br>')
culture_df["상세정보"] = culture_df["상세정보"].str.replace('<br>\n', '<br>')
culture_df["상세정보"] = culture_df["상세정보"].str.replace('\n', '<br>')
culture_df["문의 및 안내"] = culture_df["문의 및 안내"].str.replace('<br />\n', '<br>')
culture_df["문의 및 안내"] = culture_df["문의 및 안내"].str.replace('<br>\n', '<br>')
culture_df["문의 및 안내"] = culture_df["문의 및 안내"].str.replace('\n', '<br>')
for index, row in culture_df.iterrows():
    if row['전화번호'] != '':
        if row['전화번호'] != row['문의 및 안내']:
            row['문의 및 안내'] = row['전화번호']+"<br>"+row['문의 및 안내']
            print(index, row['문의 및 안내'])
    if row['광역지자체'] == '세종':
        row['기초지자체'] = '세종시'
culture_df = culture_df.drop(['전화번호'], axis=1)

with pd.ExcelWriter('../preprocessing/outputculture.xlsx', mode='w') as writer:
    culture_df.to_excel(writer, sheet_name='page1')

