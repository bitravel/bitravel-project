import openpyxl
import pandas as pd
import numpy as np


# pandas

xlsx_file = '../preprocessing/outputculture.xlsx'
sheetList = []
workbook = openpyxl.load_workbook(xlsx_file)
for sheet_name in workbook.sheetnames:
    sheetList.append(sheet_name)

xlsx = pd.ExcelFile(xlsx_file)

for i in sheetList:
    now_df = pd.read_excel(xlsx, i)
    now_df.drop(['Unnamed: 0'], axis=1, inplace=True)
    now_df.replace("\u119e", '', inplace=True)
    now_df.replace(np.NaN, '', inplace=True)
    now_df.to_csv('../preprocessing/travel.csv', sep=',', mode='a', header=False, index=False, encoding='cp949')
    print(i, 'is done.')
