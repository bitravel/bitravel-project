import re
import pandas as pd
import numpy as np
from IPython.display import display

# 제주자연 파일에 있는 관광지명을 전체 여행지 db에서 검색하여 index를 구하는 코드
for i in range(1, 2):
    path = '../searchcount/관광지 유형별 검색건수' + str(i) + '.xlsx'
    search_df = pd.read_excel(path)
    search_df = search_df.drop(['년', '전체', '공연/행사', '레포츠(육상/해상/항공)', '쇼핑'], axis=1)
    j = -1
    month = ""
    for index, row in search_df.iterrows():
        j = j + 1
        if not np.isnan(row['월']):
            month = search_df.loc[j, '월']
        else:
            search_df.loc[j, '월'] = month

    result_df = pd.DataFrame(search_df)
    result_df = result_df.drop(['월'], axis=1)
    result_df = result_df.drop(result_df.index[0:])
    # print(result_df)

    local_list = ['서울특별시', '부산광역시', '대구광역시', '인천광역시']
    df_list = []

    if i == 1:
        df_list.append(search_df[(search_df['광역지자체'] == '서울특별시')])
        df_list.append(search_df[(search_df['광역지자체'] == '부산광역시')])
        df_list.append(search_df[(search_df['광역지자체'] == '대구광역시')])
        df_list.append(search_df[(search_df['광역지자체'] == '인천광역시')])
        df_list.append(search_df[(search_df['광역지자체'] == '광주광역시')])
        print(df_list[0])
        result_df.loc[0] = ['서울특별시', df_list[0]['자연관광지'].mean(), df_list[0]['역사관광지'].mean(), df_list[0]['휴양관광지'].mean(),
                            df_list[0]['문화시설'].mean(), df_list[0]['기타관광지'].mean()]
        result_df.loc[len(result_df)] = ['부산광역시', df_list[1]['자연관광지'].mean(), df_list[1]['역사관광지'].mean(),
                                         df_list[1]['휴양관광지'].mean(), df_list[1]['문화시설'].mean(),
                                         df_list[1]['기타관광지'].mean()]
        result_df.loc[len(result_df)] = ['대구광역시', df_list[2]['자연관광지'].mean(), df_list[2]['역사관광지'].mean(),
                                         df_list[2]['휴양관광지'].mean(),
                                         df_list[2]['문화시설'].mean(), df_list[2]['기타관광지'].mean()]
        result_df.loc[len(result_df)] = ['인천광역시', df_list[3]['자연관광지'].mean(), df_list[3]['역사관광지'].mean(),
                                         df_list[3]['휴양관광지'].mean(),
                                         df_list[3]['문화시설'].mean(), df_list[3]['기타관광지'].mean()]
        result_df.loc[len(result_df)] = ['광주광역시', df_list[4]['자연관광지'].mean(), df_list[4]['역사관광지'].mean(),
                                         df_list[4]['휴양관광지'].mean(),
                                         df_list[4]['문화시설'].mean(), df_list[4]['기타관광지'].mean()]
        print(result_df)

    elif i == 2:
        daejeon_list = search_df[(search_df['광역지자체'] == '대전광역시')]
        ulsan_list = search_df[(search_df['광역지자체'] == '울산광역시')]
        sejong_list = search_df[(search_df['광역지자체'] == '세종특별자치시')]
        gyeonggi_list = search_df[(search_df['광역지자체'] == '경기도')]
        gangwon_list = search_df[(search_df['광역지자체'] == '강원도')]
    elif i == 3:
        chungbuk_list = search_df[(search_df['광역지자체'] == '충청북도')]
        chungnam_list = search_df[(search_df['광역지자체'] == '충청남도')]
        jeonbuk_list = search_df[(search_df['광역지자체'] == '전라북도')]
        jeonnam_list = search_df[(search_df['광역지자체'] == '전라남도')]
        gyeongbuk_list = search_df[(search_df['광역지자체'] == '경상북도')]
    else:
        gyeongnam_list = search_df[(search_df['광역지자체'] == '경상남도')]
        jeju_list = search_df[(search_df['광역지자체'] == '제주특별자치도')]
        korea_list = search_df[(search_df['광역지자체'] == '전국')]

#    if i == 1:
#        search_df.to_csv('../preprocessing/search_area.csv', sep=',', mode='w', header=True, index=False, encoding='cp949')
#    else:
#        search_df.to_csv('../preprocessing/search_area.csv', sep=',', mode='a', header=False, index=False, encoding='cp949')
#    print(i, 'is done.')
