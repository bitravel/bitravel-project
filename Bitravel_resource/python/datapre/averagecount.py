import pandas as pd
import numpy as np

# 관광지 유형별 검색건수 파일들에서 찾아낸 값들을 attribute 별로 평균을 내어서 정리하는 코드

local_list = ['서울특별시', '부산광역시', '대구광역시', '인천광역시', '광주광역시', '대전광역시', '울산광역시', '세종특별자치시',
              '경기도', '강원도', '충청북도', '충청남도', '전라북도', '전라남도', '경상북도', '경상남도', '제주특별자치도', '전국']
attr = ['자연관광지', '역사관광지', '휴양관광지', '문화시설', '기타관광지']

for i in range(1, 5):
    path = '../searchcount/관광지 유형별 검색건수' + str(i) + '.xlsx'
    search_df = pd.read_excel(path)
    search_df = search_df.drop(['년', '월', '전체', '공연/행사', '레포츠(육상/해상/항공)', '쇼핑'], axis=1)

    result_df = pd.DataFrame(search_df)
    result_df = result_df.drop(result_df.index[0:])

    df_list = []
    length = 5
    if i == 4:
        length = 3
    for j in range(0, length):
        # 각 지자체별 행을 모두 뽑아서 하나의 dataframe에 넣고, 그것을 df_list의 원소로 추가한다.
        df_list.append(search_df[(search_df['광역지자체'] == local_list[(i - 1) * 5 + j])])

        # 각 리스트의 Attribute 별로 평균을 구하여 한 줄 짜리 dataframe으로 만든 뒤, result_df의 맨 끝에 추가한다.
        result_df.loc[len(result_df)] = [local_list[(i - 1) * 5 + j], df_list[j][attr[0]].mean(),
                                         df_list[j][attr[1]].mean(),
                                         df_list[j][attr[2]].mean(),
                                         df_list[j][attr[3]].mean(), df_list[j][attr[4]].mean()]
    # 잘 추가되었는지 확인
    print(result_df)
    
    if i == 1:
        result_df.to_csv('../preprocessing/search_average.csv', sep=',', mode='w', header=True, index=False,
                         encoding='cp949')
    else:
        result_df.to_csv('../preprocessing/search_average.csv', sep=',', mode='a', header=False, index=False,
                         encoding='cp949')
    print(i, 'is done.')
