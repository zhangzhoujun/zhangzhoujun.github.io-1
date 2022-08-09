//
//  DateExtension.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import Foundation

extension Date {

   /// 获取当前 秒级 时间戳 - 10位
   var timeStamp : Int {
       let timeInterval: TimeInterval = self.timeIntervalSince1970
       let timeStamp = Int(timeInterval)
       return timeStamp
   }

   /// 获取当前 毫秒级 时间戳 - 13位
   var milliStamp : Int {
       let timeInterval: TimeInterval = self.timeIntervalSince1970
       let millisecond = CLongLong(round(timeInterval*1000))
       return Int(millisecond)
   }
    
    //MARK: -时间戳转时间函数
    func timeStampToString(timeStamp: Double, outputFormatter: String)->String {
       //时间戳为毫秒级要／1000 (13位数)， 秒就不用除1000（10位数），参数带没带000
        let timeString = String.init(format: "%d", timeStamp)

        let timeSta:TimeInterval

        if timeString.count == 10 {
            timeSta = TimeInterval(timeStamp)
        }else{
            timeSta = TimeInterval(timeStamp / 1000)
        }

        let date = NSDate(timeIntervalSince1970: timeSta)
        let dfmatter = DateFormatter()
        //设定时间格式,这里可以设置成自己需要的格式yyyy-MM-dd HH:mm:ss
        dfmatter.dateFormat = outputFormatter
        return dfmatter.string(from: date as Date)
    }

    //MARK: -时间转时间戳函数
    func timeToTimeStamp(time: String ,inputFormatter:String) -> Double {
        let dfmatter = DateFormatter()
       //设定时间格式,这里可以设置成自己需要的格式
        dfmatter.dateFormat = inputFormatter
        let last = dfmatter.date(from: time)
        let timeStamp = last?.timeIntervalSince1970
        return timeStamp!
    }
    
    //MARK: -时间戳转时间函数
    func timeStampToDate(timeStamp: Int)->Date {
       //时间戳为毫秒级要／1000 (13位数)， 秒就不用除1000（10位数），参数带没带000
        let timeString = String.init(format: "%d", timeStamp)

        let timeSta:TimeInterval

        if timeString.count == 10 {
            timeSta = TimeInterval(timeStamp)
        }else{
            timeSta = TimeInterval(timeStamp / 1000)
        }

        let date = NSDate(timeIntervalSince1970: timeSta)
        
        return date as Date
    }
    
    // MARK: 相差的天数
    func daysBetweenDate(startDate: Int, endDate: Int) -> Int {
        let dateStart = timeStampToDate(timeStamp: startDate)
        let dateEnd = timeStampToDate(timeStamp: endDate)
        
        let calendar = Calendar.current
        let diff:DateComponents = calendar.dateComponents([.day], from: dateStart, to: dateEnd)
        return diff.day!
    }
    
    // MARK: 相差的小时数
    func hoursBetweenDate(startDate: Int, endDate: Int) -> Int {
        let dateStart = timeStampToDate(timeStamp: startDate)
        let dateEnd = timeStampToDate(timeStamp: endDate)
        
        let calendar = Calendar.current
        let diff:DateComponents = calendar.dateComponents([.hour], from: dateStart, to: dateEnd)
        return diff.hour!
    }
}
