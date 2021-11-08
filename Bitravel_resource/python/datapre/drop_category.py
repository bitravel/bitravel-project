import pandas as pd
import os


#필요없는 소분류 카테고리 삭제하고 1~20순위만 뽑아내는 코드

path = '../popular_ta'  #엑셀파일이 모여있는 폴더
file_list = os.listdir(path) #폴더 파일 리스트
print(file_list)

for file_name_raw in file_list:

    file_name = "./popular_ta/" + file_name_raw
    popular_df = pd.read_excel(file_name, index_col=[1]) #폴더 파일 하나씩 읽어오기, unnamed코드 안만들기 위해서 인덱스열 지정
    print(popular_df)
    popular_df = popular_df.drop(['순위'], axis=1) # 소분류 카테고리가 삭제되면서 순위가 섞이기 때문에 순위열 삭제
    print(popular_df)

    mask = popular_df['소분류 카테고리'].isin(['축제', '기차역', '정자', '공항', '성당시설', '드라이브코스',
                                        '체험농가', '운동장/체육관_축구장', '관광안내소', '농장/농원', '버스터미널',
                                        '수영장_기타', '종교기타', '교회시설', '공항터미널', '항구', '약수터', '포구',
                                        '절', '성', '농/어업기타', '양식/양어장', '원불교']) #삭제할 카테고리 이름
    new1 = popular_df[~mask] # ~를 붙이면 제외 ~를 제외하면 선택한 카테고리만 선택
    new1 = new1[:20] #상위 20개만 선택
    new1 = new1.reset_index(drop=False) #지정했던 인덱스열을 빼고 순서(숫자) 적혀있는 인덱스열 생성 /drop = False 기존 인덱스열 삭제 안함
    new1.index = new1.index + 1 #순서가 0부터 시작하기에 1씩 더해줌
    new1 = new1.reset_index(drop=False) #한번더 인덱스열 새로 생성하면서 바로 전에 만들었던 인덱스 열에 열이름이 index로 생김
    new1 = new1.rename(columns={'index': '순위'}) #index 이름을 순위로 바꿈
    new1 = new1.set_index('순위') #순위가 적혀있는 열을 인덱스 열로 지정
    print(new1)
    with pd.ExcelWriter('./testp/' + file_name_raw, mode='w') as writer:
        new1.to_excel(writer) #새로운 폴더에 같은 이름 엑셀 파일로 저장 w = 덮어쓰기 , a = 추가하기