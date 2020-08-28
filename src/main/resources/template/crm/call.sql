#namespace("crm.call")
  #sql ("analysis")
          SELECT
          `owner_user_id`,
          count(call_record_id) AS `total_count_calls`,
          sum(state = 2) AS `total_count_answer`,
          sum(IF(state = 2, talk_time, 0)) AS `total_time_calls`,
          max(IF(state = 2, talk_time, 0)) AS `max_time_calls`,
          sum(type = 0) AS `total_count_calls_out`,
          sum(type = 0 && state = 2) AS `total_count_answer_out`,
          sum(
            IF (
              state = 2 && type = 0,
              talk_time,
              0
            )
          ) AS `total_time_calls_out`,
          sum(type = 1) AS `total_count_calls_in`,
          sum(type = 1 && state = 2) AS `total_count_answer_in`,
          sum(
            IF (
              state = 2 && type = 1,
              talk_time,
              0
            )
          ) AS `total_time_calls_in`,

        IF (
          count(call_record_id),
          sum(state = 2) / count(1),
          0
        ) AS `rate_answer`,

        IF (
          sum(type = 0 && state = 2),
          sum(

            IF (
              state = 2 && type = 0,
              talk_time,
              0
            )
          ) / sum(type = 0 && state = 2),
          0
        ) AS `average_time_call_out`,

        IF (
          sum(type = 0),
          sum(type = 0 && state = 2) / sum(type = 0),
          0
        ) AS `rate_answer_out`,
        IF (
          sum(type = 1 && state = 2),
          sum(

            IF (
              state = 2 && type = 1,
              talk_time,
              0
            )
          ) / sum(type = 1 && state = 2),
          0
        ) AS `average_time_call_in`,
        IF (
          sum(type = 1),
          sum(type = 1 && state = 2) / sum(type = 1),
          0
        ) AS `rate_answer_in`
        FROM
          `72crm_call_record`
        WHERE
          1 = 1
        and owner_user_id in (
          #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
          #end
         )
          #if(status == 1)
          and to_days(NOW()) = TO_DAYS(create_time)
          #end
           #if(status == 2)
          and to_days(NOW()) - TO_DAYS(create_time) = 1
          #end
           #if(status == 3)
          and YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(status == 4)
          and YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(status == 5)
          and date_format(create_time,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(status == 6)
          and date_format(create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(status == 7)
          and QUARTER(create_time)=QUARTER(now()) AND YEAR(create_time)=YEAR(NOW())
          #end
           #if(status == 8)
          and QUARTER(create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(create_time,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(status == 9)
          and YEAR(create_time)=YEAR(NOW())
          #end
           #if(status == 10)
          and YEAR(create_time)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(status == 11)
            and  TO_DAYS(create_time) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(create_time) <= TO_DAYS(#para(endTime))
          #end
        GROUP BY
          owner_user_id
  #end
#end