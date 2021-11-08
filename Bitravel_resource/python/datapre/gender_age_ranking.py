import pandas as pd
import os

#------------------성별---------------

file_name = '../gender_age/여행지별 국내여행 횟수-성별.xlsx'

travel_count_df = pd.read_excel(file_name)
travel_count_df = travel_count_df.drop(['구분1'], axis=1) #불필요한 열 제거
idx = travel_count_df[travel_count_df['서울'] == 2019].index #불필요한 행 제거
travel_count_df = travel_count_df.drop(idx)
travel_count_df = travel_count_df.rename(columns={'구분2':'성별'}, inplace=False)
travel_count_df = travel_count_df.set_index('성별')
res = travel_count_df.div(travel_count_df.sum(axis=1)/100, axis=0)

print(res.reset_index())


res.to_csv('../gender_age_result/여행지별 국내여행 횟수-성별.csv', sep=',', mode='w', header=True, index=True,
                 encoding='cp949')


#엑셀로 저장하는 코드

#with pd.ExcelWriter('./gender_age_result/여행지별 국내여행 횟수-성별.xlsx', mode = 'w') as writer:
#    res.to_excel(writer)

#------------------연령별---------------

file_name = '../gender_age/여행지별 국내여행 횟수-연령별.xlsx'

travel_count_df = pd.read_excel(file_name)
travel_count_df = travel_count_df.drop(['구분1'], axis=1)
idx = travel_count_df[travel_count_df['서울'] == 2019].index
travel_count_df = travel_count_df.drop(idx)
travel_count_df = travel_count_df.rename(columns={'구분2':'나이'}, inplace=False)
travel_count_df = travel_count_df.set_index('나이')
res = travel_count_df.div(travel_count_df.sum(axis=1)/100, axis=0)

print(res.reset_index())

res.to_csv('../gender_age_result/여행지별 국내여행 횟수-연령별.csv', sep=',', mode='w', header=True, index=True,
                 encoding='cp949')

#with pd.ExcelWriter('./gender_age_result/여행지별 국내여행 횟수-연령별.xlsx', mode = 'w') as writer:
#    res.to_excel(writer)


#폴더 내 모든 파일이 같은 구조일때 사용할 코드
'''
path = './gender_age'
file_list = os.listdir(path)
print(file_list) #제대로 파일 리스트 가져왔는지 테스트

results = []

for file_name_raw in file_list:
    file_name = "./gender_age/" + file_name_raw
    travel_count_df = pd.read_excel(file_name)
    travel_count_df = travel_count_df.drop(['구분1'], axis=1)
    idx = travel_count_df[travel_count_df['서울'] == 2019].index
    travel_count_df = travel_count_df.drop(idx)
    #travel_count_df = travel_count_df.rename(coulums={'구분2':'나이'})
    print(travel_count_df)

    travel_count_df = travel_count_df.set_index('구분2')
    res = travel_count_df.div(travel_count_df.sum(axis=1)/100, axis=0)

    print(res.reset_index())

    with pd.ExcelWriter('./gender_age_result/' + file_name_raw, mode = 'w') as writer:
        res.to_excel(writer)
'''