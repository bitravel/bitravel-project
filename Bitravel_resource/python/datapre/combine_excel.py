import pandas as pd
import glob

#폴더 내에 있는 엑셀 파일 새로운 엑셀 파일에 합치는 코드

try:
    path = '../testp/'
    files = glob.glob(path + "*.xlsx")
    excel = pd.DataFrame()
    for file_name in files:
        df = pd.read_excel(file_name)
        excel = excel.append(df, ignore_index=True)
    excel = excel.set_index('순위') #인덱스열을 지정해주면 unnamed 열 생성 안됨 (주석처리해서 테스트 해보면 됨)
    with pd.ExcelWriter('../testp/combine_file.xlsx', mode='w') as writer:
        excel.to_excel(writer)
    print(excel)


except Exception as ex:
    print('error' + str(ex))