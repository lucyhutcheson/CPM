//
//  ViewController.m
//  testApp
//
//  Created by Lucy Hutcheson on 12/10/12.
//  Copyright (c) 2012 Lucy Hutcheson. All rights reserved.
//

#import "ViewController.h"
#import "ListViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "AddViewController.h"
#import "DBManager.h"


@interface ViewController ()

@end

@implementation ViewController
@synthesize addButton,viewButton;


- (void)viewDidLoad
{
    // SETUP WITH TEMPORARY DATA
    firstName = [[NSMutableArray alloc] initWithObjects:
                 @"Simon",
                 @"Andrew",
                 @"James",
                 @"John",
                 @"Philip",
                 @"Bartholomew",
                 @"Matthew",
                 @"Thaddaeus",
                 @"Simon",
                 @"Judas",
                 nil];
    
    
    lastName = [[NSMutableArray alloc] initWithObjects:
                @"Says",
                @"Zimmerman",
                @"King",
                @"Baptist",
                @"Driver",
                @"Tiberias",
                @"Band",
                @"Smith",
                @"Zealot",
                @"Iscariot",
                nil];
    
    email = [[NSMutableArray alloc] initWithObjects:
             @"simon@says.com",
             @"andre@zimmerman.com",
             @"james@king.com",
             @"john@baptist.com",
             @"philip@driver.com",
             @"bartholomew@tiberias.com",
             @"matthew@band.com",
             @"thaddaeus@smith.com",
             @"simon@zealot.com",
             @"judas@iscariot.com",
             nil];
    
    phone = [[NSMutableArray alloc] initWithObjects:
             @"123-456-7890",
             @"456-789-0123",
             @"789-012-3456",
             @"234-567-8901",
             @"123-456-7890",
             @"456-789-0123",
             @"789-012-3456",
             @"234-567-8901",
             @"789-012-3456",
             @"234-567-8901",
             nil];
    age = [[NSMutableArray alloc] initWithObjects:
           @"18",
           @"21",
           @"24",
           @"19",
           @"23",
           @"31",
           @"27",
           @"16",
           @"25",
           @"32",
           nil];
    

    PFQuery *query = [PFQuery queryWithClassName:@"Disciple"];
    NSArray *array = [query findObjects];
    if (array.count > 0)
    {
        NSLog(@"We already have data. No need to load default data.");
        // We already have data so let's refresh it and make sure it's up to date.
        for (int i = 0; i< array.count; i++) {
            [array[i] refresh];
        }
    }
    else
    {
        NSLog(@"No data found.  Let's add some to start with.");
        // Query is empty. Load starter data.
        for (int i = 0; i < [firstName count]; i++)
        {
            [self saveToDB:i];
        }
    }
    
    // Round out custom buttons
    NSArray *buttons = [NSArray arrayWithObjects: self.addButton, self.viewButton,nil];
    
    for(UIButton *btn in buttons)
    {
        CALayer *addBtnLayer = [btn layer];
        [addBtnLayer setMasksToBounds:YES];
        [addBtnLayer setCornerRadius:7.0f];
    }
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)saveToDB:(int)index
{
    NSString *firstSave = [firstName objectAtIndex:index];
    NSString *lastSave = [lastName objectAtIndex:index];
    NSString *emailSave = [email objectAtIndex:index];
    NSString *phoneSave = [phone objectAtIndex:index];
    NSNumber *ageNum = [NSNumber numberWithInteger:[[age objectAtIndex:index] integerValue]];
    
    // SAVE INITIAL DATA TO PARSE.COM
    PFObject *disciples = [PFObject objectWithClassName:@"Disciple"];
    disciples[@"first"] = firstSave;
    disciples[@"last"] = lastSave;
    disciples[@"email"] = emailSave;
    disciples[@"phone"] = phoneSave;
    disciples[@"age"] = ageNum;
    [disciples saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (!error) {
            // The gameScore saved successfully.
        } else {
            // There was an error saving the gameScore.
            NSLog(@"%@", error);
        }
    }];
    
    // SAVE INITIAL DATA TO LOCAL SQL
    [[DBManager getSharedInstance] saveData:firstSave last:lastSave email:emailSave phone:phoneSave age:ageNum];
}


- (void)viewDidAppear:(BOOL)animated
{
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    [super viewDidAppear:true];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender
{
    UIButton *button = (UIButton *)sender;
    if (button != nil)
    {
        if (button.tag == 0)
        {
            ListViewController *listView = [[ListViewController alloc] init];
            if (listView != nil)
            {
                [self.navigationController pushViewController:listView animated:true];
            }
        }
        else if (button.tag == 1)
        {
            AddViewController *addView = [[AddViewController alloc] initWithNibName:@"AddView" bundle:nil];
            if (addView != nil)
            {
                [self.navigationController pushViewController:addView animated:true];
            }
         
        }
    }
}


@end
