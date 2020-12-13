from datetime import datetime
from datetime import date
from selenium import webdriver
import calendar
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import NoSuchElementException

options = webdriver.ChromeOptions();
options.add_argument("user-data-dir=~/.chrome_driver_session")

driver = webdriver.Chrome(options=options)
driver.get('http://web.whatsapp.com')
wait = WebDriverWait(driver, 600)
print('Entering')
c=1
my_date = date.today()
day=calendar.day_name[my_date.weekday()]
print(day)

while True:
    if(c!=0):
        try:
            print(day,day=="Tuesday")
            if (day=="Sunday"):
                inp= driver.find_element_by_class_name('_1awRl')
                inp.click()
                inp.send_keys("Kisan Broadcast",Keys.RETURN)
                inp= driver.find_elements_by_class_name('_1awRl')[1]
                inp.click()
                inp.send_keys(" _*Monson alert Heavy rains on its way*_ Keep your silos intact.",Keys.RETURN)
                c=0
            elif (day=="Friday"):
                inp= driver.find_element_by_class_name('_1awRl')
                inp.click()
                inp.send_keys("Kisan Broadcast",Keys.RETURN)
                inp= driver.find_elements_by_class_name('_1awRl')[1]
                inp.click()
                inp.send_keys(" _*Monson alert Heavy rains on its way*_ Keep your silos intact.",Keys.RETURN)
                c=0
            elif(day=="Tuesday"):
                inp= driver.find_element_by_class_name('_1awRl')
                inp.click()
                inp.send_keys("Kisan Broadcast",Keys.RETURN)
                inp= driver.find_elements_by_class_name('_1awRl')[1]
                inp.click()
                inp.send_keys(" _*Monson alert Heavy rains on its way*_ Keep your silos intact.",Keys.RETURN)
                c=0
            else:
                pass
                
        except NoSuchElementException:
            pass
    else:
        pass

