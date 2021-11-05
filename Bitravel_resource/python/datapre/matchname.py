import re
import pandas as pd
import numpy as np

# 제주자연 파일에 있는 관광지명을 전체 여행지 db에서 검색하여 index를 구하는 코드

popular_df = pd.read_excel('../popularspots/지역별 관광지 검색 순위_20211103_제주자연.xlsx')
new = popular_df['관광지명']


db_df = pd.read_csv('../preprocessing/travel.csv', encoding='cp949')
db_df.replace(np.NaN, '', inplace=True)
# print(db_df)

print(new[43])


new_reg = re.compile(new[43])

index = db_df.index[db_df['명칭'].str.contains(new_reg, regex=True)].tolist()
print(index)

