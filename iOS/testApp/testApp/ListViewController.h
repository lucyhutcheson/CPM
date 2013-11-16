//
//  ListViewController.h
//  testApp
//
//  Created by Lucy Hutcheson on 12/10/12.
//  Copyright (c) 2012 Lucy Hutcheson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddViewController.h"
#import "ListViewController.h"
#import <Parse/Parse.h>

@interface ListViewController : UIViewController <UITableViewDelegate,AddViewDelegate>
{
    IBOutlet UITableView *myTableView;
    NSMutableArray *firstName;
    NSMutableArray *lastName;
    NSMutableArray *email;
    NSMutableArray *phone;
    NSMutableArray *age;
    NSMutableArray *resultArray;
   
}
@property(nonatomic, strong) IBOutlet UIButton *addButton;

-(IBAction)sortTable:(id)sender;
-(IBAction)onClick:(id)sender;


@end
