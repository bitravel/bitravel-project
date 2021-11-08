import re
import pandas as pd

# 엑셀 파일에 있는 관광지명을 전체 여행지 db에서 검색하여 index를 구하는 코드

category_df = pd.read_excel('../searchcount/category_file.xlsx')

category_df = category_df.drop(['순위'], axis=1)
category_df = category_df.drop_duplicates(['관광지명', '상세주소'], keep='first')
category_df = category_df.reset_index(drop=False, inplace=False)
# print(category_df)
new = category_df['관광지명']
# print(new)

db_df = pd.read_csv('../preprocessing/travel_data.csv', encoding='cp949')
db_df['명칭'] = db_df['명칭'].str.replace(' ', '')
db_df["주차시설"] = db_df["주차시설"].str.replace('<br />\n', '<br>')
db_df["주차시설"] = db_df["주차시설"].str.replace('<br>\n', '<br>')
db_df["주차시설"] = db_df["주차시설"].str.replace('\n', '<br>')
db_df = db_df.drop_duplicates(['명칭', '기초지자체'], keep='first')
db_df = db_df.reset_index(drop=False, inplace=False)
t = 0
for i in range(0, len(new)):
    new_reg = re.compile(new[i])
    index = db_df.index[db_df['명칭'].str.contains(new_reg, regex=True)].tolist()
    if len(index) == 1:
        db_df.loc[index, '중분류'] = category_df.loc[i, '중분류 카테고리']
        db_df.loc[index, '소분류'] = category_df.loc[i, '소분류 카테고리']
        t = t + 1
    elif len(index) > 1:
        new_index = []
        for i_num in index:
            pa_index = db_df.loc[i_num, '명칭'].find('(')
            if category_df['상세주소'].iloc[i].find(db_df['기초지자체'].iloc[i_num]) != -1 and len(
                    db_df.loc[i_num, '명칭'][0:pa_index]) < len(new[i]) + 3:
                new_index.append(i_num)
        if len(new_index) == 1:
            db_df.loc[new_index, '중분류'] = category_df.loc[i, '중분류 카테고리']
            db_df.loc[new_index, '소분류'] = category_df.loc[i, '소분류 카테고리']
            t = t + 1
        else:
            print(new[i])
    print(t)

db_df.to_csv('../preprocessing/travel_after.csv', sep='@', mode='w', header=True, index=False, encoding='cp949')
